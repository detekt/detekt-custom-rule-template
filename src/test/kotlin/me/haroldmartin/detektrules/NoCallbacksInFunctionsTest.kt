package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
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

    @Test
    fun `does not report extension function`() {
        val code = """
private fun List<T>.forEach(function: (T) -> Unit) {
    for (i in 0 until length) {
        function(item(i))
    }
}
        """
        val findings = NoCallbacksInFunctions(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `does not report receiver function`() {
        val code = """
fun mydsl(function: String.() -> Unit) {
}
        """
        val findings = NoCallbacksInFunctions(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `does report extension function if disallowed`() {
        val code = """
private fun List<T>.forEach(function: (T) -> Unit) {
    for (i in 0 until length) {
        function(item(i))
    }
}
        """
        val findings = NoCallbacksInFunctions(KeyedConfig("allowExtensions", false)).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `does report receiver function if disallowed`() {
        val code = """
fun mydsl(function: String.() -> Unit) {
}
        """
        val findings = NoCallbacksInFunctions(KeyedConfig("allowReceivers", false)).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `does report inline function if not allowed`() {
        val code = """
inline fun myinline(function: () -> Unit) {
}
        """
        val findings = NoCallbacksInFunctions(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `does not report inline function if allowed`() {
        val code = """
inline fun myinline(function: () -> Unit) {
}
        """
        val findings = NoCallbacksInFunctions(KeyedConfig("allowInline", true)).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }
}

internal class KeyedConfig(private val key: String, private val value: Boolean) : Config {

    override fun subConfig(key: String): Config = KeyedConfig(this.key, value)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> valueOrNull(key: String): T? = when (key) {
        Config.ACTIVE_KEY -> true as? T
        this.key -> value as? T
        else -> null
    }
}
