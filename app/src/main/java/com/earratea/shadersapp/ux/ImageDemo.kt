package com.earratea.shadersapp.ux

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.earratea.shadersapp.R
import com.earratea.shadersapp.core.AgslShader.Companion.asComposeRenderEffect
import com.earratea.shadersapp.core.AgslShader.Companion.rememberAgslShader
import com.earratea.shadersapp.ui.theme.ShadersAppTheme
import org.intellij.lang.annotations.Language

@Language("AGSL")
private val ImageShader = """
    uniform float2 size;
    uniform shader composable;
    
    half4 main(in float2 fragCoord) {
        return composable.eval(fragCoord);
    }
"""

@Language("AGSL")
private val AquaShader = """
    uniform float2 size;
    uniform float time;
    uniform shader composable;
    
    
    
    half4 main(in float2 fragCoord) {
        float scale = 1 / size.x;
        float2 scaledCoord = fragCoord * scale;
        float2 center = size * 0.5 * scale;   
        float dist = distance(scaledCoord, center);
        float sin = sin(dist * 70 - time * 6);
        float2 dir = scaledCoord - center;
        float2 offset = sin * dir;
        float2 textureCoord = scaledCoord + offset / 30;
        
        //return half4(offset.xy, 0.0, 1.0);
        return composable.eval(textureCoord / scale);
    }
"""

@Composable
fun ImageDemo() {
    ImageDemoContent(
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
private fun ImageDemoContent(modifier: Modifier = Modifier) {
    val agslShader = rememberAgslShader(AquaShader)
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f
            }
        }
    }

    Image(
        painter = painterResource(id = R.drawable.image),
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
        modifier = modifier
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

@Preview
@Composable
private fun ImageDemoPreview() {
    ShadersAppTheme {
        ImageDemoContent()
    }
}