import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * A [Modifier] that only allows one click per [throttleInterval] milliseconds.
 *
 * @param throttleInterval how long to ignore further clicks after one fires (ms)
 * @param onClick the click handler to invoke
 */
fun Modifier.throttledClickable(
    throttleInterval: Long = 1_000L,
    onClick: () -> Unit
): Modifier = composed {
    // Remember the last click timestamp across recompositions
    val lastClickTime = remember { mutableStateOf(0L) }
    this.then(
        Modifier.clickable {
            val now = SystemClock.elapsedRealtime()
            if (now - lastClickTime.value >= throttleInterval) {
                lastClickTime.value = now
                onClick()
            }
        }
    )
}

/**
 * Returns a click‐handler that only lets [action] run at most once every [throttleMs] milliseconds.
 * Subsequent taps inside that window are silently ignored.
 */
@Composable
fun rememberThrottledClick(
    throttleMs: Long = 1000L,
    action: () -> Unit
): () -> Unit {
    val lastClickTime = remember { mutableLongStateOf(0L) }
    return remember {
        {
            val now = SystemClock.elapsedRealtime()
            if (now - lastClickTime.longValue >= throttleMs) {
                lastClickTime.longValue = now
                action()
            }
        }
    }
}
