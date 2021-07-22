import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    `maven-publish`
}

group = "org.example.detekt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.gitlab.arturbosch.detekt:detekt-api:1.18.0-RC2")

    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.18.0-RC2")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("junit:junit:4.13.2")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
