package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.personal.domain.entity.buildUser

class UserValidatorTest {
    @Test
    fun `given valid values when validate then build user`() {
        val user = buildUser(username = "testusername")

        assertEquals(0L, user.id)
        assertEquals("testusername", user.username)
    }

    @Test
    fun `given null username when validate then throw exception`() {
        runInvalidValuesTest(null, "Username must not be null")
    }

    @Test
    fun `given blank username when validate then throw exception`() {
        runInvalidValuesTest(" ", "Username must not be blank")
    }

    @Test
    fun `given username with more than 15 characters when validate then throw exception`() {
        runInvalidValuesTest("a".repeat(16), "Username must be shorter than 15 characters")
    }

    @Test
    fun `given invalid username when validate then throw exception`() {
        runInvalidValuesTest("testusername!", "Username can only contain letters, digits and underscore")
    }

    private fun runInvalidValuesTest(username: String?, expectedMessage: String) {
        val exception = assertThrows<IllegalArgumentException> { buildUser(username) }
        assertEquals(expectedMessage, exception.message)
    }
}