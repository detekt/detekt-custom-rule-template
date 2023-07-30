package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class NoNotNullOperatorTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports on usage of !!`() {
        val code = """
        var stuff: String? = "hi"
        val shouldError = stuff!!
        val otherStuff = requireNotNull(stuff) { "error" }
        """
        val findings = NoNotNullOperator(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `should not report on usage of optional unwrapping`() {
        val code = """
        var stuff: String? = "hi"
        val shouldNotError = stuff ?: "default"
        """
        val findings = NoNotNullOperator(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }
}
