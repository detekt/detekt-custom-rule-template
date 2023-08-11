package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class NoCallbacksInFunctionsTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `does not report functions with no callbacks`() {
        val code = """
        fun thisIsFine(s: String) {
            println(s)
        }
        fun thisIsAlsoFine() {
            println("o hai")
        }
        """
        val findings = NoCallbacksInFunctions(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `reports function with single callback`() {
        val code = """
        fun doSomething(callback1: (Float) -> Unit) {
            callback1(1f)
        }
        """
        val findings = NoCallbacksInFunctions(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `reports function with multiple callbacks`() {
        val code = """
        private fun doSomethingElse(
            parameter: List<Int>,
            callback4: (Boolean) -> Unit,
            anotherCallback5: (Any) -> Unit,
            anotherParameter: List<Int>,
        ) {
            callback(true)
        }
        """
        val findings = NoCallbacksInFunctions(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }
}
