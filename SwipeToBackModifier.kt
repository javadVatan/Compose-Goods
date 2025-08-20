package com.test.test.component.guester

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.IntOffset
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

fun Modifier.swipeToBack(
    threshold: Float = 600f,
    onBack: () -> Unit,
): Modifier = composed {
    val offsetPosition = remember { mutableFloatStateOf(0f) }
    val isDragging = remember { mutableStateOf(false) }
    val dragState = rememberDraggableState { delta ->
        offsetPosition.floatValue += delta
    }
    val interactionSource = remember { MutableInteractionSource() }

    // Animate offset
    val offsetAnim by animateFloatAsState(
        targetValue = offsetPosition.floatValue,
        animationSpec = tween(if (isDragging.value) 30 else 500)
    )

    val doBack by remember { derivedStateOf { offsetPosition.floatValue.absoluteValue > threshold } }
    LaunchedEffect(doBack) {
        if (doBack) onBack()
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is DragInteraction.Start -> isDragging.value = true
                is DragInteraction.Stop, is DragInteraction.Cancel -> {
                    offsetPosition.floatValue = 0f
                    isDragging.value = false
                }
            }
        }
    }

    this
        .draggable(
            orientation = Orientation.Vertical,
            state = dragState,
            interactionSource = interactionSource
        )
        .offset {
            IntOffset(
                0,
                offsetAnim.roundToInt())
        }
}
