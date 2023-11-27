plugins {
    kotlin("jvm") version "1.9.21"
    `maven-publish`
}

group = "org.example.detekt"
version = "1.0-SNAPSHOT"

dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.23.4")

    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.23.4")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}

kotlin {
    jvmToolchain(8)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
    systemProperty("compile-snippet-tests", project.hasProperty("compile-test-snippets"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
