package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class HbmartinRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "HbmartinRuleSet"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                AvoidFirstOrLastOnList(config),
                AvoidMutableCollections(config),
                AvoidVarsExceptWithDelegate(config),
                DontForceCast(config),
                MutableTypeShouldBePrivate(config),
                NoCallbacksInFunctions(config),
                NoNotNullOperator(config),
                NoVarsInConstructor(config),
                WhenBranchSingleLineOrBraces(config),
            ),
        )
    }
}
