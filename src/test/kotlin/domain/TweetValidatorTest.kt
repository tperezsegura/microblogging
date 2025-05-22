package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.personal.domain.entity.buildTweet

class TweetValidatorTest {
    @Test
    fun `given valid values when validate then build tweet`() {
        val tweet = buildTweet(authorId = 1L, content = "Hello!")

        assertEquals(0L, tweet.id)
        assertEquals(1L, tweet.authorId)
        assertEquals("Hello!", tweet.content)
    }

    @Test
    fun `given null author id when validate then throw exception`() {
        runInvalidValuesTest(null, "Hello!", "Author id must not be null")
    }

    @Test
    fun `given null content when validate then throw exception`() {
        runInvalidValuesTest(1L, null, "Content must not be null")
    }

    @Test
    fun `given invalid author id when validate then throw exception`() {
        runInvalidValuesTest(0L, "Hello!", "Author id must be greater than zero")
    }

    @Test
    fun `given blank content when validate then throw exception`() {
        runInvalidValuesTest(1L, " ", "Content must not be blank")
    }

    @Test
    fun `given content with more than 280 characters when validate then throw exception`() {
        runInvalidValuesTest(1L, "a".repeat(281), "Content must be shorter than 280 characters")
    }

    private fun runInvalidValuesTest(authorId: Long?, content: String?, expectedMessage: String) {
        val exception = assertThrows<IllegalArgumentException> { buildTweet(authorId, content) }
        assertEquals(expectedMessage, exception.message)
    }
}