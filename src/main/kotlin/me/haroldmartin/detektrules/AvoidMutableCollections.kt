package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.internal.RequiresTypeResolution
import org.jetbrains.kotlin.js.descriptorUtils.getKotlinTypeFqName
import org.jetbrains.kotlin.psi.KtReferenceExpression
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.resolve.bindingContextUtil.getAbbreviatedTypeOrType
import org.jetbrains.kotlin.resolve.calls.util.getType
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.supertypes

@RequiresTypeResolution
class AvoidMutableCollections(config: Config) : Rule(config) {
    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "Using mutable collections can lead to unexpected behavior.",
        debt = Debt.TWENTY_MINS,
    )

    override fun visitReferenceExpression(expression: KtReferenceExpression) {
        super.visitReferenceExpression(expression)
        if (expression.getType(bindingContext)?.isMutableCollection == true) {
            report(
                CodeSmell(
                    issue = issue,
                    entity = Entity.from(expression),
                    message = "Mutable collection type ${expression.getType(bindingContext) ?: "" } used " +
                        "in ${expression.text}",
                ),
            )
        }
    }

    override fun visitTypeReference(typeReference: KtTypeReference) {
        super.visitTypeReference(typeReference)
        if (typeReference.getAbbreviatedTypeOrType(bindingContext)?.isMutableCollection == true) {
            report(
                CodeSmell(
                    issue = issue,
                    entity = Entity.from(typeReference),
                    message = "Mutable collection type " +
                        "${typeReference.getAbbreviatedTypeOrType(bindingContext) ?: ""} used in ${typeReference.text}",
                ),
            )
        }
    }
}

private val KotlinType.isMutableCollection: Boolean
    get() = getKotlinTypeFqName(false).isMutableCollection ||
        supertypes()
            .map { it.getKotlinTypeFqName(false) }
            .any { it.isMutableCollection }

private val String.isMutableCollection: Boolean
    get() = startsWith("kotlin.collections.") && contains("Mutable")
