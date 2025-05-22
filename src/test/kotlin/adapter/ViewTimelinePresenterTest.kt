package adapter

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.personal.adapter.presenter.ViewTimelinePresenter
import org.personal.adapter.view.ResponseView
import org.personal.adapter.view.ResponseViewModel.*
import org.personal.adapter.view.TweetDto
import org.personal.application.usecase.viewtimeline.ViewTimelineResponseModel
import org.personal.domain.entity.Tweet

class ViewTimelinePresenterTest {
    private lateinit var view: ResponseView<Any>
    private lateinit var presenter: ViewTimelinePresenter

    @BeforeEach
    fun setUp() {
        view = mockk(relaxed = true)
        presenter = ViewTimelinePresenter(view)
    }

    @Test
    fun `given success response when present view timeline then display success retrieve with tweet dto list`() {
        val tweets = listOf(
            Tweet(id = 1L, authorId = 2L, content = "Hello!"),
            Tweet(id = 2L, authorId = 3L, content = "Hello!")
        )
        val response = ViewTimelineResponseModel.Success(tweets)

        presenter.presentViewTimeline(response)

        verify {
            view.displayResult(match {
                it is SuccessRetrieve && it.data is List<*> && it.data.size == 2 && it.data[0] is TweetDto && it.data[1] is TweetDto
            })
        }
    }

    @Test
    fun `given validation error when present view timeline then display bad request`() {
        val response = ViewTimelineResponseModel.Failure.ValidationError("Invalid user ID")

        presenter.presentViewTimeline(response)

        verify { view.displayResult(match { it is BadRequest && it.message == "Invalid user ID" }) }
    }

    @Test
    fun `given not found error when present view timeline then display not found`() {
        val response = ViewTimelineResponseModel.Failure.NotFoundError("User not found")

        presenter.presentViewTimeline(response)

        verify { view.displayResult(match { it is NotFound && it.message == "User not found" }) }
    }

    @Test
    fun `given unexpected error when present view timeline then display internal server error`() {
        val response = ViewTimelineResponseModel.Failure.UnexpectedError("Unexpected error")

        presenter.presentViewTimeline(response)

        verify { view.displayResult(match { it is InternalServerError && it.message == "Unexpected error" }) }
    }
}