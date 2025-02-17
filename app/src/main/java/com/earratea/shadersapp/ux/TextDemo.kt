package com.earratea.shadersapp.ux

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.earratea.shadersapp.core.AgslShader.Companion.asComposeRenderEffect
import com.earratea.shadersapp.core.AgslShader.Companion.rememberAgslShader
import com.earratea.shadersapp.ui.theme.ShadersAppTheme
import org.intellij.lang.annotations.Language

@Language("AGSL")
private val TextShader = """
    uniform float2 size;
    uniform float time;
    uniform shader composable;
    
    half4 main(in float2 fragCoord) {
        vec2 uv = fragCoord.xy / size.xy;
        uv += sin(time * vec2(1.0, 2.0) + uv * 2.0) * 0.05;
        half4 color = composable.eval(uv * size.xy);
        return half4(uv, 0.75, color.a);
    }
"""

@Composable
fun TextDemo() {
    TextDemoContent(
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
private fun TextDemoContent(
    modifier: Modifier = Modifier
) {
    val agslShader = rememberAgslShader(TextShader)
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = "Working with Shaders!",
            fontSize = 32.sp,
            modifier = Modifier
                .onSizeChanged {
                    agslShader.updateUniform {
                        setFloatUniform(
                            "size",
                            it.width.toFloat(),
                            it.height.toFloat(),
                        )
                    }
                }
                .graphicsLayer {
                    agslShader.updateUniform {
                        setFloatUniform("time", time)
                    }
                    clip = true
                    renderEffect = agslShader.asComposeRenderEffect(true)
                }
        )
    }
}

@Preview
@Composable
private fun TextDemoPreview() {
    ShadersAppTheme {
        TextDemoContent()
    }
}