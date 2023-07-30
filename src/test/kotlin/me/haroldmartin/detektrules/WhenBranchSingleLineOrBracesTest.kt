package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class WhenBranchSingleLineOrBracesTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports on branches with newline and no brace`() {
        val code = """
        val a = 1
        val b = when (a) {
            1 -> 
                "yes"
            else ->
                "no"
        }
        """
        val findings = WhenBranchSingleLineOrBraces(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 2
    }

    @Test
    fun `does not report on branches with braces`() {
        val code = """
        val a = 1
        val b = when (a) {
            1 -> {
                "yes"
            }
            else -> {
                "no"
            }
        }
        """
        val findings = WhenBranchSingleLineOrBraces(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `does not report on branches on the same line`() {
        val code = """
        val a = 1
        val b = when (a) {
            1 -> "yes"
            else -> "no"
        }
        """
        val findings = WhenBranchSingleLineOrBraces(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `report on branches on the same line with excess spacing`() {
        val code = """
        val a = 1
        val b = when (a) {
            1 ->              "yes"
            else ->           "no"
        }
        """
        val findings = WhenBranchSingleLineOrBraces(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 2
    }

    @Test
    fun `report on branches on the same line with no spacing`() {
        val code = """
        val a = 1
        val b = when (a) {
            1 ->"yes"
            else ->"no"
        }
        """
        val findings = WhenBranchSingleLineOrBraces(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 2
    }
}
