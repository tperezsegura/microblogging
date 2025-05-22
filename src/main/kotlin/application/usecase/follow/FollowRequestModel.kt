package org.personal.application.usecase.follow

data class FollowRequestModel(
    val followerId: Long?,
    val followeeId: Long?
)