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
        javaClass.simpleName,
        Severity.Defect,
        "It is dangerous to call .first() on a list since it will throw an exception" +
                "if the list is empty. Prefer to use .firstOrNull() instead.",
        Debt.FIVE_MINS,
    )

    override fun visitReferenceExpression(expression: KtReferenceExpression) {
        super.visitReferenceExpression(expression)
        val name = (expression as? KtNameReferenceExpression)?.getReferencedName()
        if (name != "first") return

        val index = expression.parent.parent.children.indexOf(expression.parent)
        if (index == 0) return

        expression.parent.parent.children.getOrNull(index - 1)?.let { previous ->
            (previous as? KtExpression)?.let { previousExpression ->
                bindingContext.getType(previousExpression)?.let { type ->
                    if (type.isList || type.constructor.supertypes.any { it.isList }) {
                        report(
                            CodeSmell(
                                issue = issue,
                                entity = Entity.from(expression),
                                message = "Too many functions can make the maintainability of a file costlier"
                            )
                        )
                    }
                }
            }
        }
    }
}

private val KotlinType.isList: Boolean
    get() = toString().startsWith("List<")
