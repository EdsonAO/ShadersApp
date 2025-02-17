package com.earratea.shadersapp.ux

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.earratea.shadersapp.core.ShaderDemoType
import com.earratea.shadersapp.ui.theme.ShadersAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ShaderScreen(
    initialDemo: ShaderDemoType = ShaderDemoType.Basic,
) {
    ShaderContent(
        initialDemo = initialDemo,
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
private fun ShaderContent(
    initialDemo: ShaderDemoType,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var selectedDemo by remember {
        mutableStateOf(initialDemo)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Shader demos",
                    modifier = Modifier
                        .padding(16.dp)
                )

                HorizontalDivider()

                ShaderDemoType.entries.fastForEach {
                    NavigationDrawerItem(
                        label = { Text(text = it.name) },
                        selected = it == selectedDemo,
                        onClick = {
                            scope.launch {
                                selectedDemo = it
                                delay(200)
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        },
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            when (selectedDemo) {
                ShaderDemoType.Basic -> BasicDemo()
                ShaderDemoType.Text -> TextDemo()
                ShaderDemoType.Image -> ImageDemo()
            }
        }
    }
}

@Preview
@Composable
private fun ShaderScreenPreview() {
    ShadersAppTheme {
        ShaderContent(initialDemo = ShaderDemoType.Basic)
    }
}