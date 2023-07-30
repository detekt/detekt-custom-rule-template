plugins {
    kotlin("jvm") version "1.9.0"
    `maven-publish`
    id("io.gitlab.arturbosch.detekt").version("1.23.1")
    jacoco
}

group = "me.haroldmartin.detektrules"
version = "1.0-SNAPSHOT"

dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.23.1")

    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.23.1")
    testImplementation("io.kotest:kotest-assertions-core:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-rules-ruleauthors:1.23.1")
    detektPlugins(rootProject)
}

kotlin {
    jvmToolchain(8)
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
