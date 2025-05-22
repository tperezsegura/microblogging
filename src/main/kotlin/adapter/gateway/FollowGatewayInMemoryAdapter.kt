package org.personal.adapter.gateway

import org.personal.application.gateway.FollowGateway
import java.util.concurrent.ConcurrentHashMap

class FollowGatewayInMemoryAdapter : FollowGateway {
    private val following = ConcurrentHashMap<Long, MutableSet<Long>>()
    private val followers = ConcurrentHashMap<Long, MutableSet<Long>>()

    override fun follow(followerId: Long, followeeId: Long) {
        following.computeIfAbsent(followerId) { mutableSetOf() }.add(followeeId)
        followers.computeIfAbsent(followeeId) { mutableSetOf() }.add(followerId)
    }

    override fun isFollowing(followerId: Long, followeeId: Long) = following[followerId].orEmpty().contains(followeeId)

    override fun findFollowers(userId: Long) = followers[userId]?.toList().orEmpty()
}