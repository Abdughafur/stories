package tj.rustamsafarovrs.stories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

/**
 * Created by Rustam Safarov on 6/18/25.
 * github.com/rustamsafarovrs
 */

@Composable
fun Stories(
    model: StoriesModel,
    controller: StoriesController = remember { StoriesController() },
    didFinish: () -> Unit = {},
) {
    var current by remember(model) {
        mutableStateOf(model.slides[model.initialSlideIndex])
    }
    var progress by remember { mutableLongStateOf(0L) }
    var slideState by remember(controller) {
        mutableStateOf<SlideState>(SlideState.Play)
    }

    // Keep controller callbacks in sync with the latest model and current slide.
    LaunchedEffect(controller, model, current, didFinish) {
        controller.next = {
            val next = model.getNext(current)
            if (next != null) {
                current = next
            } else {
                didFinish()
            }
        }

        controller.previous = {
            model.getPrevious(current)?.let { current = it }
        }

        controller.play = { slideState = SlideState.Play }
        controller.pause = { slideState = SlideState.Pause }
    }

    // Reset progress only when the active slide changes.
    LaunchedEffect(current) {
        progress = 0L
    }

    // Restart the ticker when playback is paused/resumed without losing progress.
    LaunchedEffect(current, slideState, model.config.slideDurationMs) {
        if (slideState == SlideState.Play) {
            while (progress < model.config.slideDurationMs) {
                delay(50L)
                progress = (progress + 50L).coerceAtMost(model.config.slideDurationMs)
            }
            controller.goNext()
        }
    }

    Box(Modifier.fillMaxSize()) {
        Background(current, model.config)
        ProgressIndicators(
            count = model.slides.size,
            currentIndex = model.slides.indexOf(current),
            progress = progress.toFloat() / model.config.slideDurationMs.toFloat(),
            config = model.config,
        )
        ClickArea(
            currentIndex = model.slides.indexOf(current),
            max = model.slides.size,
            config = model.config,
            previousClicked = { controller.goPrevious() },
            nextClicked = { controller.goNext() },
            playClicked = { controller.doPlay() },
            pauseClicked = { controller.doPause() },
        )
        current.overlay()
    }
}
