package org.personal.application.gateway

interface FollowGateway {
    fun follow(followerId: Long, followeeId: Long)
    fun isFollowing(followerId: Long, followeeId: Long): Boolean
    fun findFollowers(userId: Long): List<Long>
}