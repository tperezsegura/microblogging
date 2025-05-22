package org.personal.application.usecase.createtweet

data class CreateTweetRequestModel(
    val authorId: Long?,
    val content: String?
)