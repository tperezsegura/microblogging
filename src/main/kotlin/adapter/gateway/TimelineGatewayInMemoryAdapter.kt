package org.personal.adapter.gateway

import org.personal.application.gateway.TimelineGateway
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque

class TimelineGatewayInMemoryAdapter(
    private val ttl: Duration = Duration.ofMinutes(5),
    private val maxSize: Int = 10
) : TimelineGateway {
    private data class TweetEntry(val tweetId: Long, val createdAt: Instant)

    private val timelines = ConcurrentHashMap<Long, ConcurrentLinkedDeque<TweetEntry>>()

    override fun addToTimeline(userId: Long, tweetId: Long) {
        val now = Instant.now()
        val timeline = timelines.computeIfAbsent(userId) { ConcurrentLinkedDeque() }
        timeline.addFirst(TweetEntry(tweetId, now))
        removeExpiredEntries(timeline, now)
        enforceMaxSize(timeline)
    }

    override fun getTimeline(userId: Long): List<Long> = timelines[userId]?.map { it.tweetId } ?: emptyList()

    private fun removeExpiredEntries(timeline: ConcurrentLinkedDeque<TweetEntry>, now: Instant) {
        val cutoff = now.minus(ttl)
        timeline.removeIf { it.createdAt.isBefore(cutoff) }
    }

    private fun enforceMaxSize(timeline: ConcurrentLinkedDeque<TweetEntry>) {
        while (timeline.size > maxSize) timeline.pollLast()
    }
}