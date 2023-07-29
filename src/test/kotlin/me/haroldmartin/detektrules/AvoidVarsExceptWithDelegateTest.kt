package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class AvoidVarsExceptWithDelegateTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports on vars with unknown delegates`() {
        val code = """
        val shouldNotError = "hi"
        var shouldError = "hi"
        var delegated by remember { mutableStateOf(default) }
        var delegatedUnknown by notInDefaultDelegate { mutableStateOf(default) }
        """
        val findings = AvoidVarsExceptWithDelegate(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 2
    }
}
