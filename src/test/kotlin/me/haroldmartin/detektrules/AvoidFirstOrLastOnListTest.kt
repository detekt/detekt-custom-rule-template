package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class AvoidFirstOrLastOnListTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports first() call on list`() {
        val code = """
        val shouldError = listOf("hi").first()
        val testTypeImplementingList = mutableListOf("hi")
        val shouldErrorAgain = testTypeImplementingList.first()
        val shouldNotError = testTypeImplementingList.firstOrNull()
        """
        val findings = AvoidFirstOrLastOnList(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 2
    }

    @Test
    fun `reports first() call on nullable list`() {
        val code = """
        var shouldError: List<String?>? = null
        shouldError = listOf("hi")
        val emitHere = shouldError.first()
        """
        val findings = AvoidFirstOrLastOnList(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `reports last() call on list`() {
        val code = """
        val shouldError = listOf("hi").last()
        val testTypeImplementingList = mutableListOf("hi")
        val shouldErrorAgain = testTypeImplementingList.last()
        val shouldNotError = testTypeImplementingList.lastOrNull()
        """
        val findings = AvoidFirstOrLastOnList(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 2
    }

    @Test
    fun `reports first() call on kotlinx immutable list`() {
        val code = """
        import kotlinx.collections.immutable.ImmutableList
        fun testing(things: ImmutableList<String>?) {
            things?.let {
                val shouldError = things.first()
                val shouldNotError = things.firstOrNull()
            }
        }
        """
        val findings = AvoidFirstOrLastOnList(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }
}
