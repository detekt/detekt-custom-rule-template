package org.example.detekt

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtProperty

class AvoidVarsExceptWithDelegate(config: Config) : Rule(config) {
    override val issue: Issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Variables shouldn't be used except with delegates",
        Debt.TWENTY_MINS
    )

    override fun visitProperty(property: KtProperty) {
        if (property.isVar) {
            val file = property.containingKtFile
            report(
                CodeSmell(
                    issue = issue,
                    entity = Entity.from(property),
                    message = "The file ${file.name} contains `var`iable."
                )
            )
        }
        super.visitProperty(property)
    }
}
