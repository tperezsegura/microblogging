package org.personal.application.usecase.viewtimeline

import org.personal.application.gateway.TimelineGateway
import org.personal.application.gateway.TweetGateway
import org.personal.application.gateway.UserGateway
import org.personal.domain.exception.UserNotFoundException

class ViewTimelineInteractor(
    private val userGateway: UserGateway,
    private val timelineGateway: TimelineGateway,
    private val tweetGateway: TweetGateway,
    private val presenter: ViewTimelineOutputPort
) : ViewTimelineInputPort {
    override fun viewTimeline(requestModel: ViewTimelineRequestModel) {
        val result = runCatching {
            val userId = requireNotNull(requestModel.userId)
            ensureUserExists(userId)
            val tweets = retrieveTimelineTweets(userId)
            ViewTimelineResponseModel.Success(tweets)
        }.getOrElse { throwable -> ViewTimelineResponseModel.mapFailure(throwable) }
        presenter.presentViewTimeline(result)
    }

    private fun ensureUserExists(userId: Long) {
        if (!userGateway.existsById(userId)) throw UserNotFoundException(userId)
    }

    private fun retrieveTimelineTweets(userId: Long) =
        timelineGateway.getTimeline(userId).mapNotNull { tweetGateway.findById(it) }
}