package me.haroldmartin.detektrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtFunctionType
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtParameter

class NoCallbacksInFunctions(config: Config) : Rule(config) {
    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = "Use coroutines instead of callbacks.",
        debt = Debt.TWENTY_MINS,
    )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        function.functionTypeParameters?.takeIf { it.isNotEmpty() }?.let {
            report(
                CodeSmell(
                    issue = issue,
                    entity = Entity.from(function),
                    message = "${function.name} should not have callbacks: ${it.joinToString()}",
                ),
            )
        }
    }
}

@Suppress("AvoidMutableCollections")
private val KtNamedFunction.functionTypeParameters: List<String>?
    get() = valueParameterList?.run { parameters.flatMap { it.functionTypeParameters } }

private val KtParameter.functionTypeParameters: List<String>
    get() = typeReference?.run { children.mapNotNull { if (it is KtFunctionType) this.text else null } }.orEmpty()
