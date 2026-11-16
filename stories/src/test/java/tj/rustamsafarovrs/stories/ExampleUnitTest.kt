package tj.rustamsafarovrs.stories

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class StoriesModelTest {

    private fun slide(url: String) =
        StoriesModel.Slide(background = StoriesModel.Slide.Background.Url(url))

    @Test
    fun getNext_returnsFollowingSlide() {
        val first = slide("https://example.com/1.jpg")
        val second = slide("https://example.com/2.jpg")
        val model = StoriesModel(listOf(first, second))

        assertEquals(second, model.getNext(first))
    }

    @Test
    fun getNext_returnsNullForLastSlide() {
        val last = slide("https://example.com/last.jpg")
        val model = StoriesModel(listOf(last))

        assertNull(model.getNext(last))
    }

    @Test
    fun getPrevious_returnsPreviousSlide() {
        val first = slide("https://example.com/1.jpg")
        val second = slide("https://example.com/2.jpg")
        val model = StoriesModel(listOf(first, second))

        assertEquals(first, model.getPrevious(second))
    }

    @Test
    fun getPrevious_returnsNullForFirstSlide() {
        val first = slide("https://example.com/1.jpg")
        val model = StoriesModel(listOf(first))

        assertNull(model.getPrevious(first))
    }

    @Test
    fun emptySlides_areRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            StoriesModel(emptyList())
        }
    }

    @Test
    fun invalidInitialIndex_isRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            StoriesModel(
                slides = listOf(slide("https://example.com/1.jpg")),
                initialSlideIndex = 1,
            )
        }
    }
}
