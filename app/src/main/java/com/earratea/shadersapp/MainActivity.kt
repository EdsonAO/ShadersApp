package com.earratea.shadersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.earratea.shadersapp.core.ShaderDemoType
import com.earratea.shadersapp.ui.theme.ShadersAppTheme
import com.earratea.shadersapp.ux.ShaderScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShadersAppTheme {
                ShaderScreen(initialDemo = ShaderDemoType.Basic)
            }
        }
    }
}