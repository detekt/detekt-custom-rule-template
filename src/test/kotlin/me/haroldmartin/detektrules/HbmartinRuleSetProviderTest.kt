package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.kotest.matchers.shouldBe
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class HbmartinRuleSetProviderTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `verify ruleset name and rule count`() {
        val ruleSet = HbmartinRuleSetProvider()
        ruleSet.ruleSetId shouldBe "HbmartinRuleSet"
        ruleSet.instance(Config.empty).rules.size shouldBe 9
    }
}
