package org.personal.application.gateway

import org.personal.domain.entity.User

interface UserGateway {
    fun save(user: User): User
    fun existsById(userId: Long): Boolean
}