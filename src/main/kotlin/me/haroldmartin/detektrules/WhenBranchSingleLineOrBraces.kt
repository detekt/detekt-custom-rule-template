package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.psi.KtWhenEntry

class WhenBranchSingleLineOrBraces(config: Config) : Rule(config) {
    override val issue: Issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "When branches should either be on the same line or use braces.",
        debt = Debt.FIVE_MINS,
    )

    override fun visitWhenEntry(ktWhenEntry: KtWhenEntry) {
        super.visitWhenEntry(ktWhenEntry)
        (ktWhenEntry.arrow?.nextSibling as? PsiWhiteSpace)?.let { spacing ->
            when {
                spacing.text.contains("\n") -> report(
                    CodeSmell(
                        issue = issue,
                        entity = Entity.from(ktWhenEntry),
                        message = "When branch `${ktWhenEntry.text}` should either " +
                            "be on the same line or use braces.",
                    ),
                )
                spacing.text.length > 1 -> report(
                    CodeSmell(
                        issue = issue,
                        entity = Entity.from(ktWhenEntry),
                        message = "When branch `${ktWhenEntry.text}` should have a single space after " +
                            "arrow instead of ${spacing.text.length}.",
                    ),
                )
            }
        } ?: run {
            report(
                CodeSmell(
                    issue = issue,
                    entity = Entity.from(ktWhenEntry),
                    message = "When branch `${ktWhenEntry.text}` should have a single space after arrow.",
                ),
            )
        }
    }
}
