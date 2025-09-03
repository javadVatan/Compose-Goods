package com.your.domian

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.withTimeoutOrNull

@Composable
internal fun Modifier.detectSideGesture(
    onClick: (side: ClickSide) -> Unit,
    onHoldStart: () -> Unit,
    onHoldEnd: () -> Unit,
): Modifier {
    val config = LocalConfiguration.current
    val density = LocalDensity.current

    return then(
        Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val held = withTimeoutOrNull(300L) {
                            awaitRelease() // wait for finger up
                        } == null
                        if (held) {
                            onHoldStart()
                            awaitRelease()
                            onHoldEnd()
                        } else {
                            val screenHalfWidth =
                                with(density) { Dp(config.screenWidthDp.toFloat()).toPx() } / 2
                            if (offset.x < screenHalfWidth) onClick(ClickSide.Left)
                            else onClick(ClickSide.Right)
                        }
                    }
                )
            })
}

enum class ClickSide {
    Left, Right
}
