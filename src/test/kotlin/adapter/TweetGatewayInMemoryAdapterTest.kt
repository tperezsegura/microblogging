package adapter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.personal.adapter.gateway.TweetGatewayInMemoryAdapter
import org.personal.domain.entity.Tweet
import java.util.concurrent.Executors

class TweetGatewayInMemoryAdapterTest {
    private lateinit var adapter: TweetGatewayInMemoryAdapter

    @BeforeEach
    fun setup() {
        adapter = TweetGatewayInMemoryAdapter()
    }

    @Test
    fun `given valid tweet without id when save then tweet is returned with generated id`() {
        val tweet = Tweet(authorId = 1L, content = "Hello!")

        val savedTweet = adapter.save(tweet)

        assertNotEquals(0L, savedTweet.id)
    }

    @Test
    fun `given valid tweet without id when save then tweet is added to store`() {
        val tweet = Tweet(authorId = 1L, content = "Hello!")

        val savedTweet = adapter.save(tweet)

        assertEquals(savedTweet, adapter.findById(savedTweet.id))
    }

    @Test
    fun `given two distinct tweets when each is saved then each receives a unique id`() {
        val tweet = adapter.save(Tweet(authorId = 1L, content = "Hello!"))
        val otherTweet = adapter.save(Tweet(authorId = 1L, content = "Hello!"))

        assertNotEquals(tweet.id, otherTweet.id)
    }

    @Test
    fun `given two saves of tweets when saved in sequence then ids are incremented by one`() {
        val tweet = adapter.save(Tweet(authorId = 1L, content = "Hello!"))
        val otherTweet = adapter.save(Tweet(authorId = 1L, content = "Hello!"))

        assertEquals(tweet.id + 1, otherTweet.id)
    }

    @Test
    fun `given tweet already in store when save with new tweet then both tweets exist with different ids`() {
        val tweet = adapter.save(Tweet(authorId = 1L, content = "Hello!"))
        val otherTweet = adapter.save(Tweet(authorId = 1L, content = "Hello!"))

        assertNotEquals(tweet.id, otherTweet.id)
        assertEquals(tweet, adapter.findById(tweet.id))
        assertEquals(otherTweet, adapter.findById(otherTweet.id))
    }

    @Test
    fun `given concurrent saves when save on multiple threads then all tweets are saved with unique ids`() {
        val pool = Executors.newFixedThreadPool(4)
        val tweetCount = 100

        val futures = (1..tweetCount).map { i ->
            pool.submit<Tweet> { adapter.save(Tweet(authorId = 1L, content = "Tweet number $i")) }
        }
        pool.shutdown()
        val savedTweets = futures.map { it.get() }
        val ids = savedTweets.map { it.id }

        assertEquals(tweetCount, ids.toSet().size)
        savedTweets.forEach { assertEquals(it, adapter.findById(it.id)) }
    }

    @Test
    fun `given null tweet when save then throw exception`() {
        assertThrows<NullPointerException> { adapter.save(null as Tweet) }
    }

    @Test
    fun `given tweet not present when find by id then return null`() {
        assertNull(adapter.findById(999L))
    }

    @Test
    fun `given tweet previously saved when find by id with same id then return tweet`() {
        val saved = adapter.save(Tweet(authorId = 1L, content = "Hello!"))

        assertEquals(saved, adapter.findById(saved.id))
    }

    @Test
    fun `given tweet previously saved when find by id with different id then return null`() {
        adapter.save(Tweet(authorId = 1L, content = "Hello!"))

        assertNull(adapter.findById(999L))
    }

    @Test
    fun `given multiple tweets saved when find by id with each id then return each tweet`() {
        val tweets = (1..5).map { adapter.save(Tweet(authorId = 1L, content = "Tweet number $it")) }

        tweets.forEach { tweet -> assertEquals(tweet, adapter.findById(tweet.id)) }
    }
}