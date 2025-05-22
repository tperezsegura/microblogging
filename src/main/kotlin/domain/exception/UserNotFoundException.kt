package org.personal.domain.exception

class UserNotFoundException(userId: Long) : RuntimeException("User with id $userId not found")