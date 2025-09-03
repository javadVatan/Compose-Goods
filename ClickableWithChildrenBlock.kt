package com.your.packagName

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
 
@Composable
internal fun Modifier.clickableWithChildrenBlock(onClick: (position: Offset) -> Unit): Modifier {
    return then(
        Modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    event.changes.forEach { change ->
                        if (change.changedToUp()) {
                            onClick.invoke(change.position)
                            change.consume() // optionally consume to block children
                        }
                    }
                }
            }
        })
}
