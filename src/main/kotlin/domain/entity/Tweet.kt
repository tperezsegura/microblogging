package org.personal.domain.entity

import org.personal.domain.validator.TweetValidator
import java.time.Instant

data class Tweet(
    val id: Long = 0,
    val authorId: Long,
    val content: String,
    val createdAt: Instant = Instant.now()
)

fun buildTweet(authorId: Long?, content: String?): Tweet {
    TweetValidator.validate(authorId, content)
    return Tweet(authorId = authorId!!, content = content!!)
}