package adapter

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.personal.adapter.presenter.FollowPresenter
import org.personal.adapter.view.ResponseView
import org.personal.adapter.view.ResponseViewModel.*
import org.personal.application.usecase.follow.FollowResponseModel

class FollowPresenterTest {
    private lateinit var view: ResponseView<Any>
    private lateinit var presenter: FollowPresenter

    @BeforeEach
    fun setUp() {
        view = mockk(relaxed = true)
        presenter = FollowPresenter(view)
    }

    @Test
    fun `given success response when present follow then display no content`() {
        presenter.presentFollow(FollowResponseModel.Success)

        verify { view.displayResult(NoContent) }
    }

    @Test
    fun `given validation error when present follow then display bad request`() {
        val response = FollowResponseModel.Failure.ValidationError("Invalid ID")

        presenter.presentFollow(response)

        verify { view.displayResult(match { it is BadRequest && it.message == "Invalid ID" }) }
    }

    @Test
    fun `given self follow error when present follow then display bad request`() {
        val response = FollowResponseModel.Failure.SelfFollowError("Cannot follow yourself")

        presenter.presentFollow(response)

        verify { view.displayResult(match { it is BadRequest && it.message == "Cannot follow yourself" }) }
    }

    @Test
    fun `given not found error when present follow then display not found`() {
        val response = FollowResponseModel.Failure.NotFoundError("User not found")

        presenter.presentFollow(response)

        verify { view.displayResult(match { it is NotFound && it.message == "User not found" }) }
    }

    @Test
    fun `given already following error when present follow then display conflict`() {
        val response = FollowResponseModel.Failure.AlreadyFollowingError("Already following user")

        presenter.presentFollow(response)

        verify { view.displayResult(match { it is Conflict && it.message == "Already following user" }) }
    }

    @Test
    fun `given unexpected error when present follow then display internal server error`() {
        val response = FollowResponseModel.Failure.UnexpectedError("Unexpected failure")

        presenter.presentFollow(response)

        verify { view.displayResult(match { it is InternalServerError && it.message == "Unexpected failure" }) }
    }
}