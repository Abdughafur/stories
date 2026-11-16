package tj.rustamsafarovrs.stories

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable

/**
 * Created by Rustam Safarov on 6/18/25.
 * github.com/rustamsafarovrs
 */

data class StoriesModel(
    val slides: List<Slide>,
    val config: StoriesDefaults = StoriesDefaults(),
    val initialSlideIndex: Int = 0,
) {
    init {
        require(slides.isNotEmpty()) { "StoriesModel must contain at least one slide." }
        require(initialSlideIndex in slides.indices) {
            "initialSlideIndex must be a valid index in slides."
        }
        require(config.slideDurationMs > 0L) {
            "slideDurationMs must be greater than zero."
        }
    }

    fun getPrevious(current: Slide): Slide? {
        val index = slides.indexOf(current)
        return slides.getOrNull(index - 1)
    }

    fun getNext(current: Slide): Slide? {
        val index = slides.indexOf(current)
        return slides.getOrNull(index + 1)
    }

    data class Slide(
        val background: Background,
        val overlay: @Composable () -> Unit = {},
    ) {
        sealed class Background {
            data class Drawable(@DrawableRes val id: Int) : Background()
            data class Url(val url: String) : Background()
        }
    }
}

fun drawableBackground(@DrawableRes id: Int): StoriesModel.Slide.Background.Drawable {
    return StoriesModel.Slide.Background.Drawable(id)
}

fun urlBackground(url: String): StoriesModel.Slide.Background.Url {
    return StoriesModel.Slide.Background.Url(url)
}
