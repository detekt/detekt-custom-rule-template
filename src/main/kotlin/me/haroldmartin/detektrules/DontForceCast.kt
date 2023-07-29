package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtReferenceExpression

class DontForceCast(config: Config) : Rule(config) {
    override val issue: Issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "Do not use unsafe casts, safely cast with as? instead.",
        debt = Debt.TWENTY_MINS,
    )

    override fun visitReferenceExpression(expression: KtReferenceExpression) {
        super.visitReferenceExpression(expression)
        if (expression.text == "as") {
            report(
                CodeSmell(
                    issue = issue,
                    entity = Entity.from(expression),
                    message = "`${expression.parent.text}` uses an unsafe cast, safely cast with as? instead.",
                ),
            )
        }
    }
}
