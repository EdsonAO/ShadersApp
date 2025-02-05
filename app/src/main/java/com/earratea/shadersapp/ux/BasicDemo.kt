package com.earratea.shadersapp.ux

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.earratea.shadersapp.core.AgslShader.Companion.rememberAgslShader
import com.earratea.shadersapp.ui.theme.ShadersAppTheme
import org.intellij.lang.annotations.Language
import android.graphics.Color as AndroidColor

@Language("AGSL")
private val BasicShader = """
    half4 main(in float2 fragCoord) {
        return half4(1.0, 0.0, 0.0, 1.0);
    }
"""

@Language("AGSL")
private val CircleShader = """
    uniform float time;
    uniform float2 resolution;
    layout(color) uniform half4 red;
    layout(color) uniform half4 blue;
    
    half4 main(in float2 fragCoord) {
        half2 uv = fragCoord / resolution.xy;
        vec2 center = vec2(0.5, 0.5);
        float velocity = 0.8;
        
        float mixValue = distance(uv, center) + abs(sin(time * velocity));
        return mix(red, blue, mixValue);
    }
"""

@Composable
fun BasicDemo() {
    BasicDemoContent(
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
private fun BasicDemoContent(modifier: Modifier = Modifier) {
    var checked by remember { mutableStateOf(false) }
    val basicAgslShader = rememberAgslShader(BasicShader)
    val circleAgslShader = rememberAgslShader(CircleShader)
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f
            }
        }
    }

    val agslShader by remember(checked) {
        derivedStateOf { if (checked) circleAgslShader else basicAgslShader }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Text(
                text = "Animate? ",
                color = MaterialTheme.colorScheme.onBackground,
            )

            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .drawWithCache {
                    if (checked) {
                        agslShader.updateUniform {
                            setFloatUniform("time", time)
                            setFloatUniform("resolution", size.width, size.height)
                            setColorUniform("red", AndroidColor.RED)
                            setColorUniform("blue", AndroidColor.BLUE)
                        }
                    }

                    onDrawBehind {
                        drawRect(agslShader.brush)
                    }
                }
                .size(200.dp)
        ) {
            Text(
                text = "Basic!",
                color = Color.White,
                fontSize = 24.sp
            )
        }
    }
}

@Preview
@Composable
private fun BasicDemoPreview() {
    ShadersAppTheme {
        BasicDemoContent()
    }
}