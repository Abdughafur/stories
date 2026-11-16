package tj.rustamsafarovrs.stories

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Created by Rustam Safarov on 6/19/25.
 * github.com/rustamsafarovrs
 */

@Composable
internal fun ClickArea(
    currentIndex: Int,
    max: Int,
    config: StoriesDefaults,
    previousClicked: () -> Unit,
    nextClicked: () -> Unit,
    playClicked: () -> Unit,
    pauseClicked: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(currentIndex, max, config) {
                var pausedByGesture = false
                detectTapGestures(
                    onPress = {
                        try {
                            tryAwaitRelease()
                        } finally {
                            if (pausedByGesture) {
                                playClicked()
                                pausedByGesture = false
                            }
                        }
                    },
                    onLongPress = {
                        if (config.clickToPauseEnabled != false) {
                            pausedByGesture = true
                            pauseClicked()
                        }
                    },
                    onTap = { offset ->
                        if (config.clickToChangeEnabled == false) {
                            return@detectTapGestures
                        }
                        if (offset.x < size.width / 2) {
                            if (currentIndex > 0) {
                                previousClicked()
                            }
                        } else if (currentIndex < max - 1) {
                            nextClicked()
                        }
                    },
                )
            },
    )
}
