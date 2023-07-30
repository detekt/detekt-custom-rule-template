package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class AvoidMutableCollectionsTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `does not report on immutable declarations`() {
        val code = """
        val immutableSet = setOf<String>()
        val immutableSet = listOf<String>()
        val immutableMap = mapOf<String, String>()
        """
        val findings = AvoidMutableCollections(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `reports on mutable declarations`() {
        val code = """
        val mutableSet = mutableSetOf<String>()
        val mutableSet = mutableListOf<String>()
        val mutableMap = mutableMapOf<String, String>()
        """
        val findings = AvoidMutableCollections(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 3
    }

    @Test
    fun `does not report on immutable function return types`() {
        val code = """
        fun immutableSet(): Set<String> = setOf()
        fun immutableList(): List<String> = listOf()
        fun immutableMap(): Map<String, String> = mapOf()
        """
        val findings = AvoidMutableCollections(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `reports on mutable function return types`() {
        val code = """
        fun mutableSet(): MutableSet<String> = mutableSetOf()
        fun mutableList(): MutableList<String> = mutableListOf()
        fun mutableMap(): MutableMap<String, String> = mutableMapOf()
        """
        val findings = AvoidMutableCollections(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 6
    }

    @Test
    fun `does not report on immutable class fields`() {
        val code = """
        data class BadBadNotGood(
            val immutableSet: Set<String>,
            val immutableList: List<String>,
            val immutableMap: Map<String, String>,
        )
        """
        val findings = AvoidMutableCollections(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `reports on mutable class fields`() {
        val code = """
        data class BadBadNotGood(
            val mutableSet: MutableSet<String>,
            val mutableList: MutableList<String>,
            val mutableMap: MutableMap<String, String>,
        )
        """
        val findings = AvoidMutableCollections(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 3
    }
}
