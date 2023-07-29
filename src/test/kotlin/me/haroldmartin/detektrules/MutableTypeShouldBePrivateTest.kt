package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class MutableTypeShouldBePrivateTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports exposed mutable type`() {
        val code = """
        class MyViewModel {
            val shouldError = MutableStateFlow(0)
            val shouldNotError = shouldError.asStateFlow()
            private val shouldNotError2 = MutableStateFlow(0)
        }
        """
        val findings = MutableTypeShouldBePrivate(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }
}
