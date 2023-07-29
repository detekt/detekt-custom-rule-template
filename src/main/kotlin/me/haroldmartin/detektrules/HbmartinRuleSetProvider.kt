package org.example.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class HbmartinRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "HbmartinRuleSet"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                AvoidFirstOnListRule(config),
            ),
        )
    }
}
