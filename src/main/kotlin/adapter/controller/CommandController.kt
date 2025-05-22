package org.personal.adapter.controller

import org.personal.application.usecase.createtweet.CreateTweetInputPort
import org.personal.application.usecase.createtweet.CreateTweetRequestModel
import org.personal.application.usecase.createuser.CreateUserInputPort
import org.personal.application.usecase.createuser.CreateUserRequestModel

class CommandController(
    private val createUserInteractor: CreateUserInputPort,
    private val createTweetInteractor: CreateTweetInputPort,
) {
    fun createUser(username: String?) = createUserInteractor.createUser(
        CreateUserRequestModel(username)
    )

    fun createTweet(authorId: Long?, content: String?) = createTweetInteractor.createTweet(
        CreateTweetRequestModel(authorId, content)
    )
}