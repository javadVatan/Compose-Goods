package com.your.packagName

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput

internal fun Modifier.clickableWithChildrenBlock(onClick: () -> Unit): Modifier {
    return then(Modifier.pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Initial)
                if (event.changes.any { it.changedToUp() }) {
                    onClick.invoke()
                }
                event.changes.forEach { it.consume() }
            }
        }
    })
}
