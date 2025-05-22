package org.personal.adapter.gateway

import org.personal.application.gateway.TweetGateway
import org.personal.domain.entity.Tweet
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class TweetGatewayInMemoryAdapter : TweetGateway {
    private val tweetsById = ConcurrentHashMap<Long, Tweet>()
    private val idSequence = AtomicLong(1)

    override fun save(tweet: Tweet): Tweet {
        val generatedId = idSequence.getAndIncrement()
        val savedTweet = tweet.copy(id = generatedId)
        tweetsById[generatedId] = savedTweet
        return savedTweet
    }

    override fun findById(tweetId: Long) = tweetsById[tweetId]
}