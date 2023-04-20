#!/usr/bin/env bash

github_repository=$1
owner=$(echo $github_repository | cut -d/ -f1 | tr '[:upper:]' '[:lower:]')
name=$(echo $github_repository | cut -d/ -f2 | tr '[:upper:]' '[:lower:]' | tr -d "-" | tr -d ".")
package_name="com.github.${owner}.${name}"
directory=$(echo $package_name | tr "." "/")
repo_name=$(echo $github_repository | cut -d/ -f2)

mkdir -p src/main/kotlin/$directory
mkdir -p src/test/kotlin/$directory

find src/main src/test -type f -exec sed -i "s/org.example.detekt/$package_name/g" {} +

sed -i "s/org.example.detekt/com.github.${owner}/g" build.gradle.kts gradle.properties
sed -i "s/detekt-custom-rule/$repo_name/g" settings.gradle.kts gradle.properties
sed -i "s|github.com/example/custom-rule|github.com/$github_repository|g" gradle.properties

mv src/main/kotlin/org/example/detekt/* src/main/kotlin/$directory
mv src/test/kotlin/org/example/detekt/* src/test/kotlin/$directory

rm $0
rm .github/workflows/cleanup.yaml

git reset
git add src/main src/test build.gradle.kts settings.gradle.kts $0 .github/workflows/cleanup.yaml
git commit -m "Change package name from org.example.detekt to $package_name"
