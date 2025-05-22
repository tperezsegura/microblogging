package application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.personal.application.gateway.UserGateway
import org.personal.application.usecase.createuser.CreateUserInteractor
import org.personal.application.usecase.createuser.CreateUserOutputPort
import org.personal.application.usecase.createuser.CreateUserRequestModel
import org.personal.application.usecase.createuser.CreateUserResponseModel
import org.personal.domain.entity.User

class CreateUserInteractorTest {
    private lateinit var userGateway: UserGateway
    private lateinit var presenter: CreateUserOutputPort
    private lateinit var interactor: CreateUserInteractor

    @BeforeEach
    fun setUp() {
        userGateway = mockk()
        presenter = mockk(relaxed = true)
        interactor = CreateUserInteractor(userGateway, presenter)
    }

    @Test
    fun `given valid username when create user then success response is presented with saved user`() {
        val request = CreateUserRequestModel(username = "testusername")
        val savedUser = User(id = 1L, username = "testusername")
        every { userGateway.save(any()) } returns savedUser

        interactor.createUser(request)

        verify {
            presenter.presentUserCreation(match { it is CreateUserResponseModel.Success && it.user == savedUser })
        }
    }

    @Test
    fun `given invalid username when create user then validation failure is presented`() {
        interactor.createUser(CreateUserRequestModel(username = ""))

        verify {
            presenter.presentUserCreation(match { it is CreateUserResponseModel.Failure.ValidationError })
        }
    }

    @Test
    fun `given unexpected error when create user then unexpected failure is presented`() {
        every { userGateway.save(any()) } throws RuntimeException()

        interactor.createUser(CreateUserRequestModel(username = "testusername"))

        verify {
            presenter.presentUserCreation(match { it is CreateUserResponseModel.Failure.UnexpectedError })
        }
    }
}