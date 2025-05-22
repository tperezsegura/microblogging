package adapter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.personal.adapter.gateway.FollowGatewayInMemoryAdapter

class FollowGatewayInMemoryAdapterTest {
    private lateinit var adapter: FollowGatewayInMemoryAdapter

    @BeforeEach
    fun setup() {
        adapter = FollowGatewayInMemoryAdapter()
    }

    @Test
    fun `given two user ids when follow then follow relationship is created`() {
        adapter.follow(1L, 2L)

        assertTrue(adapter.isFollowing(1L, 2L))
    }

    @Test
    fun `given user already follows another user when follow again then no change occurs`() {
        adapter.follow(1L, 2L)
        adapter.follow(1L, 2L)

        assertEquals(listOf(1L), adapter.findFollowers(2L))
    }

    @Test
    fun `given user does not follow another user when is following then return false`() {
        assertFalse(adapter.isFollowing(1L, 2L))
    }


    @Test
    fun `given user with followers when find followers then return all followers`() {
        adapter.follow(1L, 2L)
        adapter.follow(3L, 2L)

        val followers = adapter.findFollowers(2L)

        assertEquals(setOf(1L, 3L), followers.toSet())
    }
    @Test
    fun `given user with no followers when find followers then return empty list`() {
        assertTrue(adapter.findFollowers(999L).isEmpty())
    }

    @Test
    fun `given two distinct users with followers when find followers then return corresponding followers`() {
        adapter.follow(1L, 2L)
        adapter.follow(3L, 4L)

        assertTrue(adapter.findFollowers(2L).contains(1L))
        assertTrue(adapter.findFollowers(4L).contains(3L))
        assertFalse(adapter.findFollowers(2L).contains(3L))
        assertFalse(adapter.findFollowers(4L).contains(1L))
    }

}