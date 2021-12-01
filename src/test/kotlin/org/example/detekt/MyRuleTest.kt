package org.example.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class CustomRuleSpec {

    @Test
    fun `reports inner classes`() {
        val code = """
        class A {
          inner class B
        }
        """
        val findings = MyRule(Config.empty).compileAndLint(code)
        assertThat(findings).hasSize(1)
    }

    @Test
    fun `doesn't report inner classes`() {
        val code = """
        class A {
          class B
        }
        """
        val findings = MyRule(Config.empty).compileAndLint(code)
        assertThat(findings).isEmpty()
    }
}
