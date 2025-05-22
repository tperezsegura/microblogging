package application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.personal.application.gateway.TimelineGateway
import org.personal.application.gateway.TweetGateway
import org.personal.application.gateway.UserGateway
import org.personal.application.usecase.viewtimeline.ViewTimelineInteractor
import org.personal.application.usecase.viewtimeline.ViewTimelineOutputPort
import org.personal.application.usecase.viewtimeline.ViewTimelineRequestModel
import org.personal.application.usecase.viewtimeline.ViewTimelineResponseModel
import org.personal.domain.entity.Tweet

class ViewTimelineInteractorTest {
    private lateinit var userGateway: UserGateway
    private lateinit var timelineGateway: TimelineGateway
    private lateinit var tweetGateway: TweetGateway
    private lateinit var presenter: ViewTimelineOutputPort
    private lateinit var interactor: ViewTimelineInteractor

    @BeforeEach
    fun setUp() {
        userGateway = mockk()
        timelineGateway = mockk()
        tweetGateway = mockk()
        presenter = mockk(relaxed = true)
        interactor = ViewTimelineInteractor(userGateway, timelineGateway, tweetGateway, presenter)
    }

    @Test
    fun `given valid user with tweets when view timeline then success response is presented with tweets`() {
        val userId = 1L
        val tweetIds = listOf(1L, 2L, 3L)
        val tweets = tweetIds.map { Tweet(id = it, authorId = userId, content = "Tweet number $it") }
        every { userGateway.existsById(userId) } returns true
        every { timelineGateway.getTimeline(userId) } returns tweetIds
        tweetIds.forEachIndexed { index, id -> every { tweetGateway.findById(id) } returns tweets[index] }

        interactor.viewTimeline(ViewTimelineRequestModel(userId))

        verify {
            presenter.presentViewTimeline(match { it is ViewTimelineResponseModel.Success && it.tweets == tweets })
        }
    }

    @Test
    fun `given valid user with no tweets when view timeline then success response is presented with empty list`() {
        val userId = 1L
        every { userGateway.existsById(userId) } returns true
        every { timelineGateway.getTimeline(userId) } returns emptyList()

        interactor.viewTimeline(ViewTimelineRequestModel(userId))

        verify {
            presenter.presentViewTimeline(match { it is ViewTimelineResponseModel.Success && it.tweets.isEmpty() })
        }
    }

    @Test
    fun `given user does not exist when view timeline then user not found failure is presented`() {
        val userId = 1L
        every { userGateway.existsById(userId) } returns false

        interactor.viewTimeline(ViewTimelineRequestModel(userId))

        verify {
            presenter.presentViewTimeline(match { it is ViewTimelineResponseModel.Failure.NotFoundError })
        }
    }

    @Test
    fun `given null user id in request when view timeline then validation failure is presented`() {
        interactor.viewTimeline(ViewTimelineRequestModel(userId = null))

        verify {
            presenter.presentViewTimeline(match { it is ViewTimelineResponseModel.Failure.ValidationError })
        }
    }

    @Test
    fun `given unexpected error when view timeline then unexpected failure is presented`() {
        val userId = 1L
        every { timelineGateway.getTimeline(userId) } throws RuntimeException()

        interactor.viewTimeline(ViewTimelineRequestModel(userId))

        verify {
            presenter.presentViewTimeline(match { it is ViewTimelineResponseModel.Failure.UnexpectedError })
        }
    }
}