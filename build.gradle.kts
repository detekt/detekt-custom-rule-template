plugins {
    kotlin("jvm") version "1.9.21"
    `maven-publish`
    alias(libs.plugins.detekt)
    jacoco
    id("com.github.ben-manes.versions") version "0.50.0"
}

group = "me.haroldmartin.detektrules"
version = "0.1.4"

dependencies {
    compileOnly(libs.detekt.api)

    testImplementation(libs.detekt.test)
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.6")

    detektPlugins(libs.detekt.formatting)
    detektPlugins(libs.detekt.ruleauthors)
    detektPlugins(rootProject)
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
    systemProperty("compile-snippet-tests", project.hasProperty("compile-test-snippets"))
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(false)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

detekt {
    allRules = true
}
