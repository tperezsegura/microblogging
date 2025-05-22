package adapter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.personal.adapter.gateway.TimelineGatewayInMemoryAdapter
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.Executors

class TimelineGatewayInMemoryAdapterTest {
    private lateinit var adapter: TimelineGatewayInMemoryAdapter

    @BeforeEach
    fun setUp() {
        adapter = TimelineGatewayInMemoryAdapter()
    }

    @Test
    fun `given empty timeline when add to timeline then tweet appears in timeline`() {
        val userId = 1L
        val tweetId = 1L

        adapter.addToTimeline(userId, tweetId)

        assertEquals(listOf(tweetId), adapter.getTimeline(userId))
    }

    @Test
    fun `given multiple tweets when add to timeline then timeline is in reverse order`() {
        val userId = 1L
        val tweets = (1L..5L).toList()

        tweets.forEach { adapter.addToTimeline(userId, it) }

        assertEquals(tweets.reversed(), adapter.getTimeline(userId))
    }

    @Test
    fun `given two users when each adds tweets then timelines are independent`() {
        val userId = 1L
        val otherUserId = 2L
        val tweetId = 1L
        val otherTweetId = 2L

        adapter.addToTimeline(userId, tweetId)
        adapter.addToTimeline(otherUserId, otherTweetId)

        assertEquals(listOf(tweetId), adapter.getTimeline(userId))
        assertEquals(listOf(otherTweetId), adapter.getTimeline(otherUserId))
    }

    @Test
    fun `given unknown user when get timeline then return empty list`() {
        assertTrue(adapter.getTimeline(999L).isEmpty())
    }

    @Test
    fun `given more than max size tweets when add to timeline then timeline size is max size and oldest tweets are removed`() {
        val userId = 1L
        val maxSize = 10
        val tweetIds = (1L..(maxSize + 5)).toList()

        tweetIds.forEach { adapter.addToTimeline(userId, it) }

        val result = adapter.getTimeline(userId)
        assertEquals(maxSize, result.size)
        assertEquals(tweetIds.takeLast(maxSize).reversed(), result)
    }

    @Test
    fun `given old timeline entries when add to timeline then expired tweets are removed`() {
        val adapter = TimelineGatewayInMemoryAdapter(ttl = Duration.ofMinutes(1), maxSize = 10)
        val userId = 1L
        val oldTime = Instant.now().minus(Duration.ofMinutes(2))
        val recentTime = Instant.now()
        val timelineField = adapter.javaClass.getDeclaredField("timelines")
        timelineField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val timelines = timelineField.get(adapter) as ConcurrentHashMap<Long, ConcurrentLinkedDeque<*>>
        val tweetEntryConstructor =
            Class.forName("org.personal.adapter.gateway.TimelineGatewayInMemoryAdapter\$TweetEntry")
                .getDeclaredConstructor(Long::class.java, Instant::class.java)
        tweetEntryConstructor.isAccessible = true
        val oldTweet = tweetEntryConstructor.newInstance(100L, oldTime)
        val recentTweet = tweetEntryConstructor.newInstance(101L, recentTime)
        val deque = ConcurrentLinkedDeque<Any>()
        deque.add(oldTweet)
        deque.add(recentTweet)
        timelines[userId] = deque

        adapter.addToTimeline(userId, 102L)

        val result = adapter.getTimeline(userId)
        assertEquals(listOf(102L, 101L), result)
    }

    @Test
    fun `given concurrent additions when add to timeline then all tweets are present`() {
        val adapter = TimelineGatewayInMemoryAdapter(ttl = Duration.ofMinutes(5), maxSize = 200)
        val pool = Executors.newFixedThreadPool(4)
        val userId = 1L
        val tweetCount = 100

        val futures = (1..tweetCount).map { i -> pool.submit { adapter.addToTimeline(userId, i.toLong()) } }
        pool.shutdown()
        futures.forEach { it.get() }

        val timeline = adapter.getTimeline(userId)
        assertEquals(tweetCount, timeline.toSet().size)
        (1..tweetCount).forEach { tweetId -> assertTrue(timeline.contains(tweetId.toLong())) }
    }
}