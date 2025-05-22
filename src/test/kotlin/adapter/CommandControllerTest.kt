package adapter

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.personal.adapter.controller.CommandController
import org.personal.application.usecase.createtweet.CreateTweetInputPort
import org.personal.application.usecase.createtweet.CreateTweetRequestModel
import org.personal.application.usecase.createuser.CreateUserInputPort
import org.personal.application.usecase.createuser.CreateUserRequestModel
import org.personal.application.usecase.follow.FollowInputPort
import org.personal.application.usecase.follow.FollowRequestModel

class CommandControllerTest {
    private lateinit var createUser: CreateUserInputPort
    private lateinit var createTweet: CreateTweetInputPort
    private lateinit var follow: FollowInputPort
    private lateinit var controller: CommandController

    @BeforeEach
    fun setUp() {
        createUser = mockk(relaxed = true)
        createTweet = mockk(relaxed = true)
        follow = mockk(relaxed = true)
        controller = CommandController(createUser, createTweet, follow)
    }

    @Test
    fun `given username when create user then delegate to create user interactor with request model`() {
        assertDoesNotThrow { controller.createUser("testusername") }
        verify { createUser.createUser(CreateUserRequestModel("testusername")) }
    }

    @Test
    fun `given author id and content when create tweet then delegate to create tweet interactor with request model`() {
        assertDoesNotThrow { controller.createTweet(1L, "Hello!") }
        verify { createTweet.createTweet(CreateTweetRequestModel(1L, "Hello!")) }
    }

    @Test
    fun `given follower and followee ids when follow then delegate to follow interactor with request model`() {
        assertDoesNotThrow { controller.follow(1L, 2L) }
        verify { follow.follow(FollowRequestModel(1L, 2L)) }
    }
}
