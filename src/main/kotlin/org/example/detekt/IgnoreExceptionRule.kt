package org.example.detekt

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.anyDescendantOfType
import org.jetbrains.kotlin.psi.psiUtil.lastBlockStatementOrThis

class IgnoreExceptionRule(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Defect,
        "This rule reports ignored exceptions in catch block",
        Debt.FIVE_MINS,
    )

    override fun visitCatchSection(catchClause: KtCatchClause) {
        super.visitCatchSection(catchClause)
        // if empty catch block then default to report
        val catchBody = catchClause.catchBody ?: return

        val hasIf = catchBody.anyDescendantOfType<KtIfExpression>()
        val hasWhen = catchBody.anyDescendantOfType<KtWhenExpression>()

        val lastStatement = catchBody.lastBlockStatementOrThis()
        if ((hasIf || hasWhen) && lastStatement !is KtThrowExpression) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(catchClause),
                    "Exception is ignored"
                )
            )
        }
    }
}
