package org.example.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class AvoidFirstOnListRuleTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports first() call on list`() {
        val code = """
        val testList = listOf("hi")
        val shouldError = testList.first()
        val testTypeImplementingList = mutableListOf("hi")
        val shouldErrorAgain = testTypeImplementingList.first()
        """
        val findings = AvoidFirstOnListRule(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 2
    }
}
