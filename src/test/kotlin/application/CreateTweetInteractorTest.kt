package application

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.personal.application.gateway.FollowGateway
import org.personal.application.gateway.TimelineGateway
import org.personal.application.gateway.TweetGateway
import org.personal.application.gateway.UserGateway
import org.personal.application.usecase.createtweet.CreateTweetOutputPort

import org.personal.application.usecase.createtweet.CreateTweetInteractor
import org.personal.application.usecase.createtweet.CreateTweetRequestModel
import org.personal.application.usecase.createtweet.CreateTweetResponseModel
import org.personal.domain.entity.Tweet

class CreateTweetInteractorTest {
    private lateinit var userGateway: UserGateway
    private lateinit var tweetGateway: TweetGateway
    private lateinit var followGateway: FollowGateway
    private lateinit var timelineGateway: TimelineGateway
    private lateinit var presenter: CreateTweetOutputPort
    private lateinit var interactor: CreateTweetInteractor

    @BeforeEach
    fun setUp() {
        userGateway = mockk()
        tweetGateway = mockk()
        followGateway = mockk()
        timelineGateway = mockk()
        presenter = mockk(relaxed = true)
        interactor = CreateTweetInteractor(userGateway, tweetGateway, followGateway, timelineGateway, presenter)
    }

    @Test
    fun `given valid tweet when create tweet then tweet is saved, timelines are updated and success response is presented with saved tweet`() {
        val request = CreateTweetRequestModel(authorId = 1L, content = "Hello!")
        val savedTweet = Tweet(id = 10L, content = "Hello!", authorId = 1L)
        val followers = listOf(2L, 3L)
        every { userGateway.existsById(1L) } returns true
        every { tweetGateway.save(any()) } returns savedTweet
        every { followGateway.findFollowers(1L) } returns followers
        every { timelineGateway.addToTimeline(any(), any()) } just Runs

        interactor.createTweet(request)

        verifySequence {
            userGateway.existsById(1L)
            tweetGateway.save(match { it.content == "Hello!" && it.authorId == 1L })
            timelineGateway.addToTimeline(1L, 10L)
            followGateway.findFollowers(1L)
            timelineGateway.addToTimeline(2L, 10L)
            timelineGateway.addToTimeline(3L, 10L)
            presenter.presentTweetCreation(match { it is CreateTweetResponseModel.Success && it.tweet == savedTweet })
        }
    }

    @Test
    fun `given invalid tweet when create tweet then validation failure is presented`() {
        interactor.createTweet(CreateTweetRequestModel(authorId = 1L, content = ""))

        verify {
            presenter.presentTweetCreation(match { it is CreateTweetResponseModel.Failure.ValidationError })
        }
    }

    @Test
    fun `given author does not exists when create tweet then user not found failure is presented`() {
        every { userGateway.existsById(1L) } returns false

        interactor.createTweet(CreateTweetRequestModel(authorId = 1L, content = "Hello!"))

        verify {
            presenter.presentTweetCreation(match { it is CreateTweetResponseModel.Failure.NotFoundError })
        }
    }

    @Test
    fun `given unexpected error when create tweet then user not found failure is presented`() {
        every { userGateway.existsById(1L) } throws RuntimeException()

        interactor.createTweet(CreateTweetRequestModel(authorId = 1L, content = "Hello!"))

        verify {
            presenter.presentTweetCreation(match { it is CreateTweetResponseModel.Failure.UnexpectedError })
        }
    }
}