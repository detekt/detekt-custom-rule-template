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
    fun `reports callbacks`() {
        val code = """
        fun doNothing(s: String) {
            println(s)
        }
        fun doSomething(callback1: (Float) -> Unit) {
            callback1(1f)
        }
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
        findings shouldHaveSize 2
    }
}
