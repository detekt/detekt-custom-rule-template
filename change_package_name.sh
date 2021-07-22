#!/usr/bin/env bash

package_name=$1
directory=$(echo $package_name | tr "." "/")

mkdir -p src/main/kotlin/$directory
mkdir -p src/test/kotlin/$directory

find src/main src/test -type f -exec sed -i s/org.example.detekt/$package_name/g {} +
sed -i s/org.example.detekt/$package_name/g build.gradle.kts

mv src/main/kotlin/org/example/detekt/* src/main/kotlin/$directory
mv src/test/kotlin/org/example/detekt/* src/test/kotlin/$directory

rm $0

git reset
git add src/main src/test build.gradle.kts $0
git commit -m "Change package name from org.example.detekt to $package_name"
