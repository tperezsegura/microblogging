package org.personal.adapter.controller

import org.personal.application.usecase.createuser.CreateUserInputPort
import org.personal.application.usecase.createuser.CreateUserRequestModel
import org.personal.application.usecase.createtweet.CreateTweetInputPort
import org.personal.application.usecase.createtweet.CreateTweetRequestModel
import org.personal.application.usecase.follow.FollowInputPort
import org.personal.application.usecase.follow.FollowRequestModel

class CommandController(
    private val createUserInteractor: CreateUserInputPort,
    private val createTweetInteractor: CreateTweetInputPort,
    private val followInteractor: FollowInputPort
) {
    fun createUser(username: String?) = createUserInteractor.createUser(
        CreateUserRequestModel(username)
    )

    fun createTweet(authorId: Long?, content: String?) = createTweetInteractor.createTweet(
        CreateTweetRequestModel(authorId, content)
    )

    fun follow(followerId: Long?, followeeId: Long?) = followInteractor.follow(
        FollowRequestModel(followerId, followeeId)
    )
}