package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class NoVarsInConstructorTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports on vars in a class constructor`() {
        val code = """
        class BadBadNotGood(val x: String, var y: Int, var z: Int)
        """
        val findings = NoVarsInConstructor(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 2
    }
}

class BadBadNotGood(val x: String, var y: Int, var z: Int)
