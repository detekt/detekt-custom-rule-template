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
}
