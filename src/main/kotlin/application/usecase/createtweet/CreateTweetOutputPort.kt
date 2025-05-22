package org.personal.application.usecase.createtweet

interface CreateTweetOutputPort {
    fun presentTweetCreation(responseModel: CreateTweetResponseModel)
}