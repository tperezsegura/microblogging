package application

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.personal.application.gateway.FollowGateway
import org.personal.application.gateway.UserGateway
import org.personal.application.usecase.follow.FollowInteractor
import org.personal.application.usecase.follow.FollowOutputPort
import org.personal.application.usecase.follow.FollowRequestModel
import org.personal.application.usecase.follow.FollowResponseModel


class FollowInteractorTest {
    private lateinit var userGateway: UserGateway
    private lateinit var followGateway: FollowGateway
    private lateinit var presenter: FollowOutputPort
    private lateinit var interactor: FollowInteractor

    @BeforeEach
    fun setUp() {
        userGateway = mockk()
        followGateway = mockk()
        presenter = mockk(relaxed = true)
        interactor = FollowInteractor(userGateway, followGateway, presenter)
    }

    @Test
    fun `given valid users not following when follow then success response is presented`() {
        val userId = 1L
        val otherUserId = 2L
        every { userGateway.existsById(userId) } returns true
        every { userGateway.existsById(otherUserId) } returns true
        every { followGateway.isFollowing(userId, otherUserId) } returns false
        every { followGateway.follow(userId, otherUserId) } just Runs

        interactor.follow(FollowRequestModel(followerId = userId, followeeId = otherUserId))

        verify { followGateway.follow(userId, otherUserId) }
        verify { presenter.presentFollow(FollowResponseModel.Success) }
    }

    @Test
    fun `given same follower and followee when follow then self follow not allowed failure is presented`() {
        interactor.follow(FollowRequestModel(followerId = 1L, followeeId = 1L))

        verify { presenter.presentFollow(match { it is FollowResponseModel.Failure.SelfFollowError }) }
        verify(exactly = 0) { followGateway.follow(any(), any()) }
    }

    @Test
    fun `given follower does not exist when follow then user not found failure is presented`() {
        every { userGateway.existsById(1L) } returns false

        interactor.follow(FollowRequestModel(followerId = 1L, followeeId = 2L))

        verify { presenter.presentFollow(match { it is FollowResponseModel.Failure.NotFoundError }) }
    }

    @Test
    fun `given followee does not exist when follow then user not found failure is presented`() {
        every { userGateway.existsById(1L) } returns true
        every { userGateway.existsById(2L) } returns false

        interactor.follow(FollowRequestModel(followerId = 1L, followeeId = 2L))

        verify { presenter.presentFollow(match { it is FollowResponseModel.Failure.NotFoundError }) }
    }

    @Test
    fun `given users already following when follow then already following failure is presented`() {
        every { userGateway.existsById(1L) } returns true
        every { userGateway.existsById(2L) } returns true
        every { followGateway.isFollowing(1L, 2L) } returns true

        interactor.follow(FollowRequestModel(followerId = 1L, followeeId = 2L))

        verify { presenter.presentFollow(match { it is FollowResponseModel.Failure.AlreadyFollowingError }) }
    }

    @Test
    fun `given null follower id when follow then validation failure is presented`() {
        interactor.follow(FollowRequestModel(followerId = null, followeeId = 2L))

        verify { presenter.presentFollow(match { it is FollowResponseModel.Failure.ValidationError }) }
    }

    @Test
    fun `given null followee id when follow then validation failure is presented`() {
        interactor.follow(FollowRequestModel(followerId = 1L, followeeId = null))

        verify { presenter.presentFollow(match { it is FollowResponseModel.Failure.ValidationError }) }
    }

    @Test
    fun `given unexpected error when follow then unexpected failure is presented`() {
        every { followGateway.isFollowing(1L, 2L) } throws RuntimeException()

        interactor.follow(FollowRequestModel(followerId = 1L, followeeId = 2L))

        verify { presenter.presentFollow(match { it is FollowResponseModel.Failure.UnexpectedError }) }
    }
}