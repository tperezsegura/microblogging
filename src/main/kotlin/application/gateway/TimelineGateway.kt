package org.personal.application.gateway

interface TimelineGateway {
    fun addToTimeline(userId: Long, tweetId: Long)
    fun getTimeline(userId: Long): List<Long>
}