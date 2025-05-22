package org.personal.application.usecase.follow

import org.personal.application.gateway.FollowGateway
import org.personal.application.gateway.UserGateway
import org.personal.domain.exception.AlreadyFollowingException
import org.personal.domain.exception.SelfFollowNotAllowedException
import org.personal.domain.exception.UserNotFoundException

class FollowInteractor(
    private val userGateway: UserGateway,
    private val followGateway: FollowGateway,
    private val presenter: FollowOutputPort
) : FollowInputPort {
    override fun follow(requestModel: FollowRequestModel) {
        val result = runCatching {
            val followerId = requireNotNull(requestModel.followerId)
            val followeeId = requireNotNull(requestModel.followeeId)
            validateFollow(followerId, followeeId)
            followGateway.follow(followerId, followeeId)
            FollowResponseModel.Success
        }.getOrElse { throwable -> FollowResponseModel.mapFailure(throwable) }
        presenter.presentFollow(result)
    }

    private fun validateFollow(followerId: Long, followeeId: Long) {
        if (followerId == followeeId) throw SelfFollowNotAllowedException()
        ensureUserExists(followerId)
        ensureUserExists(followeeId)
        if (followGateway.isFollowing(followerId, followeeId)) throw AlreadyFollowingException()
    }

    private fun ensureUserExists(userId: Long) {
        if (!userGateway.existsById(userId)) throw UserNotFoundException(userId)
    }
}