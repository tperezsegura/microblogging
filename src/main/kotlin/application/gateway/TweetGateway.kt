package org.personal.application.gateway

import org.personal.domain.entity.Tweet

interface TweetGateway {
    fun save(tweet: Tweet): Tweet
    fun findById(tweetId: Long): Tweet?
}