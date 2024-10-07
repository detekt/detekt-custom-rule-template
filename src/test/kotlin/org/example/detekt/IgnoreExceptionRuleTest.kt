package org.example.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class IgnoreExceptionRuleTest(private val env: KotlinCoreEnvironment) {

    @Test
    fun `should not report when exceptions is thrown (if)`() {
        // language=kotlin
        val code = """
            fun test() {
                try {
                    throw Exception()
                } catch (e: Exception) {
                    if (e is NumberFormatException) {
                        println("handle exception")
                    }
                    throw e
                }
            }
        """
        val findings = IgnoreExceptionRule(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `should not report when exceptions is thrown (when)`() {
        // language=kotlin
        val code = """
            fun test() {
                try {
                    throw Exception()
                } catch (e: Exception) {
                    when (e) {
                        is NumberFormatException -> println("handle exception")
                    }
                    throw e
                }
            }
        """
        val findings = IgnoreExceptionRule(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `should report ignored exceptions in catch block with if expression`() {
        // language=kotlin
        val code = """
            fun test() {
                try {
                    throw Exception()
                } catch (e: Exception) {
                    if (e is NumberFormatException) {
                        println("handle exception")
                    }
                    println("Exception is ignored")
                }
            }
        """
        val findings = IgnoreExceptionRule(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
        findings[0].message shouldBe "Exception is ignored"
    }

    @Test
    fun `should report ignored exceptions in catch block with when expression`() {
        // language=kotlin
        val code = """
            fun test() {
                try {
                    throw Exception()
                } catch (e: Exception) {
                    when (e) {
                        is NumberFormatException -> println("handle exception")
                    }
                    println("Exception is ignored")
                }
            }
        """
        val findings = IgnoreExceptionRule(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
        findings[0].message shouldBe "Exception is ignored"
    }

    @Test
    fun `should report when exceptions is thrown in if expression and not thrown in outside the block`() {
        // language=kotlin
        val code = """
            fun test() {
                try {
                    throw Exception()
                } catch (e: Exception) {
                    if (e is NumberFormatException) {
                        throw e
                    }
                    println("Exception is ignored")
                }
            }
        """
        val findings = IgnoreExceptionRule(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
        findings[0].message shouldBe "Exception is ignored"
    }

    @Test
    fun `should report when exceptions is thrown in when expression and not thrown in outside the block`() {
        // language=kotlin
        val code = """
            fun test() {
                try {
                    throw Exception()
                } catch (e: Exception) {
                    when (e) {
                        is NumberFormatException -> throw e
                    }
                    println("Exception is ignored")
                }
            }
        """
        val findings = IgnoreExceptionRule(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
        findings[0].message shouldBe "Exception is ignored"
    }

}
