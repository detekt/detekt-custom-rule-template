package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.isPrivate

class MutableTypeShouldBePrivate(config: Config) : Rule(config) {
    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = "Do not expose as public variables Mutable* types, e.g. MutableStateFlow",
        debt = Debt.FIVE_MINS,
    )

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
        if (property.isPrivate()) return
        property.guessType()?.let { type ->
            if (type.startsWith("Mutable")) {
                report(
                    CodeSmell(
                        issue = issue,
                        entity = Entity.from(property),
                        message = "${property.name ?: "Property"} should be private since it is a mutable type.",
                    ),
                )
            }
        }
    }

    // Guess type from type reference or infer it from the initializer.
    private fun KtProperty.guessType() = typeReference?.text ?: initializer?.firstChild?.text
}
