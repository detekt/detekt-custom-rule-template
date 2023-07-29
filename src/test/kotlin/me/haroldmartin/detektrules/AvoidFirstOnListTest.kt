package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class AvoidFirstOnListTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports first() call on list`() {
        val code = """
        val shouldError = listOf("hi").first()
        val testTypeImplementingList = mutableListOf("hi")
        val shouldErrorAgain = testTypeImplementingList.first()
        val shouldNotError = testTypeImplementingList.firstOrNull()
        """
        val findings = AvoidFirstOnList(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 2
    }
}
