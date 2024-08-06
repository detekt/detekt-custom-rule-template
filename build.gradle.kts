plugins {
    kotlin("jvm") version "2.0.0"
    `maven-publish`
    alias(libs.plugins.detekt)
    jacoco
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "me.haroldmartin.detektrules"
version = "0.1.4"

dependencies {
    compileOnly(libs.detekt.api)

    testImplementation(libs.detekt.test)
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")

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
