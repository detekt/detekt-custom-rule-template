package org.example.detekt

import com.google.common.truth.Truth.assertThat
import io.github.detekt.test.utils.KotlinCoreEnvironmentWrapper
import io.github.detekt.test.utils.createEnvironment
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

internal class MyRuleSpec {

    @Test
    fun `reports inner classes`() {
        val code = """
        class A {
          inner class B
        }
        """
        val findings = MyRule(Config.empty).compileAndLintWithContext(env, code)
        assertThat(findings).hasSize(1)
    }

    @Test
    fun `doesn't report inner classes`() {
        val code = """
        class A {
          class B
        }
        """
        val findings = MyRule(Config.empty).compileAndLintWithContext(env, code)
        assertThat(findings).isEmpty()
    }

    private val env: KotlinCoreEnvironment
        get() = envWrapper.env

    companion object {
        private lateinit var envWrapper: KotlinCoreEnvironmentWrapper

        @BeforeClass
        @JvmStatic
        fun setUp() {
            envWrapper = createEnvironment()
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            envWrapper.dispose()
        }
    }
}
