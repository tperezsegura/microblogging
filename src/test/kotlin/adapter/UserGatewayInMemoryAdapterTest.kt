package adapter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.personal.adapter.gateway.UserGatewayInMemoryAdapter
import org.personal.domain.entity.User
import java.util.concurrent.Executors

class UserGatewayInMemoryAdapterTest {
    private lateinit var adapter: UserGatewayInMemoryAdapter

    @BeforeEach
    fun setUp() {
        adapter = UserGatewayInMemoryAdapter()
    }

    @Test
    fun `given valid user without id when save then user is returned with generated id`() {
        val user = User(username = "testusername")

        val savedUser = adapter.save(user)

        assertNotEquals(0L, savedUser.id)
    }

    @Test
    fun `given valid user without id when save then user is added to store`() {
        val user = User(username = "testusername")

        val savedUser = adapter.save(user)

        assertTrue(adapter.existsById(savedUser.id))
    }

    @Test
    fun `given two distinct users when each is saved then each receives a unique id`() {
        val user = adapter.save(User(username = "testusername"))
        val otherUser = adapter.save(User(username = "othertestusername"))

        assertNotEquals(user.id, otherUser.id)
    }

    @Test
    fun `given two saves of users when saved in sequence then ids are incremented by one`() {
        val user = adapter.save(User(username = "testusername"))
        val otherUser = adapter.save(User(username = "othertestusername"))

        assertEquals(user.id + 1, otherUser.id)
    }

    @Test
    fun `given user already in store when save with new user then both users exist with different ids`() {
        val user = adapter.save(User(username = "testusername"))
        val otherUser = adapter.save(User(username = "othertestusername"))

        assertTrue(adapter.existsById(user.id))
        assertTrue(adapter.existsById(otherUser.id))
        assertNotEquals(user.id, otherUser.id)
    }

    @Test
    fun `given concurrent saves when save on multiple threads then all users are saved with unique ids`() {
        val pool = Executors.newFixedThreadPool(4)
        val userCount = 100

        val futures = (1..userCount).map { i -> pool.submit<User> { adapter.save(User(username = "testusername$i")) } }
        pool.shutdown()
        val savedUsers = futures.map { it.get() }
        val ids = savedUsers.map { it.id }

        assertEquals(userCount, ids.toSet().size)
        savedUsers.forEach { assertTrue(adapter.existsById(it.id)) }
    }

    @Test
    fun `given null user when save then throw exception`() {
        assertThrows<NullPointerException> { adapter.save(null as User) }
    }

    @Test
    fun `given user not present when exists by id then return false`() {
        assertFalse(adapter.existsById(999L))
    }

    @Test
    fun `given user previously saved when exists by id with same id then return true`() {
        val saved = adapter.save(User(username = "testusername"))

        assertTrue(adapter.existsById(saved.id))
    }

    @Test
    fun `given user previously saved when exists by id with different id then return false`() {
        adapter.save(User(username = "testusername"))

        assertFalse(adapter.existsById(999L))
    }

    @Test
    fun `given multiple users saved when exists by id with each id then return true for each`() {
        val ids = (1..5).map { adapter.save(User(username = "testusername$it")).id }

        ids.forEach { assertTrue(adapter.existsById(it)) }
    }
}