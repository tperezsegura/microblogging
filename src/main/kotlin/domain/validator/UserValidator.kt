package org.personal.domain.validator

object UserValidator {
    fun validate(username: String?) {
        require(username != null) { "Username must not be null" }
        require(username.isNotBlank()) { "Username must not be blank" }
        require(username.length <= 15) { "Username must be shorter than 15 characters" }
        require(username.all { it.isLetterOrDigit() || it == '_' }) { "Username can only contain letters, digits and underscore" }
    }
}