package adapter

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.personal.adapter.presenter.CreateUserPresenter
import org.personal.adapter.view.ResponseView
import org.personal.adapter.view.ResponseViewModel.*
import org.personal.adapter.view.UserDto
import org.personal.application.usecase.createuser.CreateUserResponseModel
import org.personal.domain.entity.User

class CreateUserPresenterTest {
    private lateinit var view: ResponseView<Any>
    private lateinit var presenter: CreateUserPresenter

    @BeforeEach
    fun setUp() {
        view = mockk(relaxed = true)
        presenter = CreateUserPresenter(view)
    }

    @Test
    fun `given success response when present user creation then display success create with user dto`() {
        val user = User(id = 1L, username = "testusername")
        val response = CreateUserResponseModel.Success(user)

        presenter.presentUserCreation(response)

        verify { view.displayResult(match { it is SuccessCreate && it.data is UserDto }) }
    }

    @Test
    fun `given validation error response when present user creation then display bad request`() {
        val response = CreateUserResponseModel.Failure.ValidationError("Invalid username")

        presenter.presentUserCreation(response)

        verify { view.displayResult(match { it is BadRequest && it.message == "Invalid username" }) }
    }

    @Test
    fun `given unexpected error response when present user creation then display internal server error`() {
        val response = CreateUserResponseModel.Failure.UnexpectedError("Unexpected error")

        presenter.presentUserCreation(response)

        verify { view.displayResult(match { it is InternalServerError && it.message == "Unexpected error" }) }
    }
}