package org.personal.adapter.gateway

import org.personal.application.gateway.UserGateway
import org.personal.domain.entity.User
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class UserGatewayInMemoryAdapter : UserGateway {
    private val usersById = ConcurrentHashMap<Long, User>()
    private val idSequence = AtomicLong(1)

    override fun save(user: User): User {
        val generatedId = idSequence.getAndIncrement()
        val savedUser = user.copy(id = generatedId)
        usersById[generatedId] = savedUser
        return savedUser
    }

    override fun existsById(userId: Long): Boolean = usersById.containsKey(userId)
}