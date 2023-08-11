package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.config
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.findDescendantOfType

class AvoidVarsExceptWithDelegate(config: Config) : Rule(config) {
    override val issue: Issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "Variables shouldn't be used except with delegates",
        debt = Debt.TWENTY_MINS,
    )

    private val allowedDelegates: List<Regex> by config(listOf("remember\\w*", "mutableState\\w*")) {
        it.map(String::toRegex)
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
        if (!property.isVar) return
        property.delegate?.let { delegate ->
            val delegateName = delegate
                .findDescendantOfType<KtNameReferenceExpression>()
                ?.getReferencedName()

            if (delegateName == null || !allowedDelegates.any { it.matches(delegateName) }) {
                report(
                    CodeSmell(
                        issue = issue,
                        entity = Entity.from(property),
                        message = "Property ${property.name} is a delegated `var`iable but the delegate is not " +
                            "allowed. Change to val or configure allowed delegates regex list",
                    ),
                )
            }
        } ?: run {
            report(
                CodeSmell(
                    issue = issue,
                    entity = Entity.from(property),
                    message = "Property ${property.name.orEmpty()} is a `var`iable, please make it a val.",
                ),
            )
        }
    }
}
