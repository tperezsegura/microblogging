package org.personal.application.usecase.createtweet

interface CreateTweetInputPort {
    fun createTweet(requestModel: CreateTweetRequestModel)
}