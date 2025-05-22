package org.personal.domain.entity

import org.personal.domain.validator.UserValidator

data class User(
    val id: Long = 0,
    val username: String
)

fun buildUser(username: String?): User {
    UserValidator.validate(username)
    return User(username = username!!)
}