package adapter

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.personal.adapter.controller.CommandController
import org.personal.application.usecase.createuser.CreateUserInputPort
import org.personal.application.usecase.createuser.CreateUserRequestModel

class CommandControllerTest {
    private lateinit var createUser: CreateUserInputPort
    private lateinit var controller: CommandController

    @BeforeEach
    fun setUp() {
        createUser = mockk(relaxed = true)
        controller = CommandController(createUser)
    }

    @Test
    fun `given username when create user then delegate to create user interactor with request model`() {
        assertDoesNotThrow { controller.createUser("testusername") }
        verify { createUser.createUser(CreateUserRequestModel("testusername")) }
    }
}
