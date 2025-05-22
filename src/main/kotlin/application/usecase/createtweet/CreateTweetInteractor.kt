package org.personal.application.usecase.createtweet

import org.personal.application.gateway.FollowGateway
import org.personal.application.gateway.TimelineGateway
import org.personal.application.gateway.TweetGateway
import org.personal.application.gateway.UserGateway
import org.personal.domain.entity.Tweet
import org.personal.domain.entity.buildTweet
import org.personal.domain.exception.UserNotFoundException

class CreateTweetInteractor(
    private val userGateway: UserGateway,
    private val tweetGateway: TweetGateway,
    private val followGateway: FollowGateway,
    private val timelineGateway: TimelineGateway,
    private val presenter: CreateTweetOutputPort
) : CreateTweetInputPort {
    override fun createTweet(requestModel: CreateTweetRequestModel) {
        val result = runCatching {
            val tweet = buildTweet(requestModel.authorId, requestModel.content)
            ensureUserExists(tweet.authorId)
            val savedTweet = tweetGateway.save(tweet)
            updateTimelines(savedTweet)
            CreateTweetResponseModel.Success(savedTweet)
        }.getOrElse { throwable -> CreateTweetResponseModel.mapFailure(throwable) }
        presenter.presentTweetCreation(result)
    }

    private fun ensureUserExists(authorId: Long) {
        if (!userGateway.existsById(authorId)) throw UserNotFoundException(authorId)
    }

    private fun updateTimelines(savedTweet: Tweet) {
        timelineGateway.addToTimeline(savedTweet.authorId, savedTweet.id)
        followGateway.findFollowers(savedTweet.authorId)
            .forEach { followerId -> timelineGateway.addToTimeline(followerId, savedTweet.id) }
    }
}