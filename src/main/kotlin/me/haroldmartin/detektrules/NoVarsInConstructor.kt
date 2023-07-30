package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.identifierName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.allConstructors

class NoVarsInConstructor(config: Config) : Rule(config) {
    override val issue: Issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "Do not use `var`s in a class constructor.",
        debt = Debt.FIVE_MINS,
    )

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        klass.allConstructors.forEach { constructor ->
            constructor.valueParameters.forEach { parameter ->
                if (parameter.isMutable) {
                    report(
                        CodeSmell(
                            issue = issue,
                            entity = Entity.from(constructor),
                            message = "Disallowed `var` parameter ${parameter.text} in constructor for ${klass.identifierName()}, please make it a val.",
                        ),
                    )
                }
            }
        }
    }
}
