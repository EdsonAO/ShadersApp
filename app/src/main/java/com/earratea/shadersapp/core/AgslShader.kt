package com.earratea.shadersapp.core

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asComposeRenderEffect
import org.intellij.lang.annotations.Language

class AgslShader(code: String) {

    private val shader = RuntimeShader(code)
    val brush = ShaderBrush(shader)

    fun updateUniform(uniform: RuntimeShader.() -> Unit) {
        shader.apply(uniform)
    }

    companion object {
        const val ComposableUniform = "composable"
        fun String.createAgslShader(): AgslShader {
            return AgslShader(this.trimIndent())
        }

        fun AgslShader.asComposeRenderEffect(setComposable: Boolean = false) = when (setComposable) {
            true -> RenderEffect.createRuntimeShaderEffect(shader, ComposableUniform)
            false -> RenderEffect.createShaderEffect(shader)
        }.asComposeRenderEffect()

        @Composable
        fun rememberAgslShader(@Language("AGSL") stringShader: String) = remember {
            stringShader.createAgslShader()
        }
    }
}
