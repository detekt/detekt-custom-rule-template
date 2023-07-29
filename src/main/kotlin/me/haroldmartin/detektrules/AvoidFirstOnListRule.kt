package org.example.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.types.KotlinType

class AvoidFirstOnListRule(config: Config) : Rule(config) {
    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = "It is dangerous to call .first() on a list since it will throw an exception" +
                "if the list is empty. Prefer to use .firstOrNull() instead.",
        debt = Debt.FIVE_MINS,
    )

    override fun visitReferenceExpression(expression: KtReferenceExpression) {
        super.visitReferenceExpression(expression)
        val name = (expression as? KtNameReferenceExpression)?.getReferencedName()
        if (name != "first") return

        val index = expression.parent.parent.children.indexOf(expression.parent)
        if (index == 0) return

        expression.parent.parent.children.getOrNull(index - 1)
            ?.let { it as? KtExpression }
            ?.let { bindingContext.getType(it) }
            ?.takeIf { it.isList  }
            ?.run {
                report(
                    CodeSmell(
                        issue = issue,
                        entity = Entity.from(expression),
                        message = "Prefer to use .firstOrNull() instead of .first()"
                    )
                )
            }
    }
}

private val KotlinType.isList: Boolean
    get() = toString().startsWith("List<") || constructor.supertypes.any { it.isList }
