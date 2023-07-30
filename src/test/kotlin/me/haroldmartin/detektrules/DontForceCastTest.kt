package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class DontForceCastTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports unsafe casting`() {
        val code = """
        val y = 1
        val shouldError: String = y as String
        val shouldNotError: String? = y as? String
        """
        val findings = DontForceCast(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `reports unsafe casting for sealed classes`() {
        val code = """
        sealed class LoadingState<T : Any> {
            class Loading<T : Any> : LoadingState<T>()
            data class Success<T : Any>(val payload: T) : LoadingState<T>()
            data class ErrorOnLoad<T : Any>(val error: TextResource) : LoadingState<T>()
        }
        
        fun shouldError(state: LoadingState<Int>): Unit {
            val isLoading = state is LoadingState.Loading
            val payload = if (!isLoading) {
                (state as LoadingState.Success).payload
            } else {
                null
            }
        }
        """
        val findings = DontForceCast(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }
}
