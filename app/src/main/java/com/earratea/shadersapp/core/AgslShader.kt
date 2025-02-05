package com.earratea.shadersapp.core

import android.graphics.RuntimeShader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ShaderBrush
import org.intellij.lang.annotations.Language

class AgslShader(code: String) {

    private val shader = RuntimeShader(code)
    val brush = ShaderBrush(shader)

    fun updateUniform(uniform: RuntimeShader.() -> Unit) {
        shader.apply(uniform)
    }

    companion object {
        fun String.createAgslShader(): AgslShader {
            return AgslShader(this.trimIndent())
        }

        @Composable
        fun rememberAgslShader(@Language("AGSL") stringShader: String) = remember {
            stringShader.createAgslShader()
        }
    }
}
