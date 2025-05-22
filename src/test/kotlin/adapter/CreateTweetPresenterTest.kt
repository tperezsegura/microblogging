package adapter

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.personal.adapter.presenter.CreateTweetPresenter
import org.personal.adapter.view.ResponseView
import org.personal.adapter.view.ResponseViewModel.*
import org.personal.adapter.view.TweetDto
import org.personal.application.usecase.createtweet.CreateTweetResponseModel
import org.personal.domain.entity.Tweet

class CreateTweetPresenterTest {
    private lateinit var view: ResponseView<Any>
    private lateinit var presenter: CreateTweetPresenter

    @BeforeEach
    fun setUp() {
        view = mockk(relaxed = true)
        presenter = CreateTweetPresenter(view)
    }

    @Test
    fun `given success response when present tweet creation then display success create with tweet dto`() {
        val tweet = Tweet(id = 1L, authorId = 10L, content = "Hello!")
        val response = CreateTweetResponseModel.Success(tweet)

        presenter.presentTweetCreation(response)

        verify { view.displayResult(match { it is SuccessCreate && it.data is TweetDto }) }
    }

    @Test
    fun `given validation error response when present tweet creation then display bad request`() {
        val response = CreateTweetResponseModel.Failure.ValidationError("Invalid content")

        presenter.presentTweetCreation(response)

        verify { view.displayResult(match { it is BadRequest && it.message == "Invalid content" }) }
    }

    @Test
    fun `given not found error response when present tweet creation then display not found`() {
        val response = CreateTweetResponseModel.Failure.NotFoundError("User not found")

        presenter.presentTweetCreation(response)

        verify { view.displayResult(match { it is NotFound && it.message == "User not found" }) }
    }

    @Test
    fun `given unexpected error response when present tweet creation then display internal server error`() {
        val response = CreateTweetResponseModel.Failure.UnexpectedError("Unexpected error")

        presenter.presentTweetCreation(response)

        verify { view.displayResult(match { it is InternalServerError && it.message == "Unexpected error" }) }
    }
}
