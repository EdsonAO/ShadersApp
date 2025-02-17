package com.earratea.shadersapp.ux.complex

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import com.earratea.shadersapp.core.AgslShader.Companion.asComposeRenderEffect
import com.earratea.shadersapp.core.AgslShader.Companion.rememberAgslShader
import com.earratea.shadersapp.ui.theme.ShadersAppTheme

@Composable
fun ComplexDemo() {
    ComplexDemoContent(
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
private fun ComplexDemoContent(modifier: Modifier = Modifier) {
    val agslShader = rememberAgslShader(ComplexShader1)
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 4000f
            }
        }
    }

    Surface (
        modifier = modifier
            .background(Color.Black)
            .onSizeChanged {
                agslShader.updateUniform {
                    setFloatUniform(
                        "resolution",
                        it.width.toFloat(),
                        it.height.toFloat(),
                    )
                }
            }
            .graphicsLayer {
                agslShader.updateUniform {
                    setFloatUniform("time", time)
                }
                renderEffect = agslShader.asComposeRenderEffect()
            }
    ) {}
}

@Preview
@Composable
private fun ComplexDemoPreview() {
    ShadersAppTheme {
        ComplexDemoContent()
    }
}