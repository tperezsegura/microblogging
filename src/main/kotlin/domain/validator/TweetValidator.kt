package org.personal.domain.validator

object TweetValidator {
    fun validate(authorId: Long?, content: String?) {
        require(authorId != null) { "Author id must not be null" }
        require(content != null) { "Content must not be null" }
        require(authorId > 0) { "Author id must be greater than zero" }
        require(content.isNotBlank()) { "Content must not be blank" }
        require(content.length <= 280) { "Content must be shorter than 280 characters" }
    }
}