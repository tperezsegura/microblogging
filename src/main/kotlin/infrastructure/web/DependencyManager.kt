package org.personal.infrastructure.web

import org.personal.adapter.controller.CommandController
import org.personal.adapter.controller.QueryController
import org.personal.adapter.gateway.FollowGatewayInMemoryAdapter
import org.personal.adapter.gateway.TimelineGatewayInMemoryAdapter
import org.personal.adapter.gateway.TweetGatewayInMemoryAdapter
import org.personal.adapter.gateway.UserGatewayInMemoryAdapter
import org.personal.adapter.presenter.CreateTweetPresenter
import org.personal.adapter.presenter.CreateUserPresenter
import org.personal.adapter.presenter.FollowPresenter
import org.personal.adapter.presenter.ViewTimelinePresenter
import org.personal.adapter.view.ResponseView
import org.personal.application.gateway.FollowGateway
import org.personal.application.gateway.TimelineGateway
import org.personal.application.gateway.TweetGateway
import org.personal.application.gateway.UserGateway
import org.personal.application.usecase.createtweet.CreateTweetInputPort
import org.personal.application.usecase.createtweet.CreateTweetInteractor
import org.personal.application.usecase.createtweet.CreateTweetOutputPort
import org.personal.application.usecase.createuser.CreateUserInputPort
import org.personal.application.usecase.createuser.CreateUserInteractor
import org.personal.application.usecase.createuser.CreateUserOutputPort
import org.personal.application.usecase.follow.FollowInputPort
import org.personal.application.usecase.follow.FollowInteractor
import org.personal.application.usecase.follow.FollowOutputPort
import org.personal.application.usecase.viewtimeline.ViewTimelineInteractor
import org.personal.application.usecase.viewtimeline.ViewTimelineOutputPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.web.context.WebApplicationContext

@Configuration
open class DependencyManager {
    @Bean
    open fun createUserInteractor(
        userGateway: UserGateway,
        presenter: CreateUserOutputPort
    ) = CreateUserInteractor(userGateway, presenter)

    @Bean
    open fun createTweetInteractor(
        userGateway: UserGateway,
        tweetGateway: TweetGateway,
        followGateway: FollowGateway,
        timelineGateway: TimelineGateway,
        presenter: CreateTweetOutputPort
    ) = CreateTweetInteractor(userGateway, tweetGateway, followGateway, timelineGateway, presenter)

    @Bean
    open fun followInteractor(
        userGateway: UserGateway,
        followGateway: FollowGateway,
        presenter: FollowOutputPort
    ) = FollowInteractor(userGateway, followGateway, presenter)

    @Bean
    open fun viewTimelineInteractor(
        userGateway: UserGateway,
        timelineGateway: TimelineGateway,
        tweetGateway: TweetGateway,
        presenter: ViewTimelineOutputPort
    ) = ViewTimelineInteractor(userGateway, timelineGateway, tweetGateway, presenter)

    @Bean
    open fun commandController(
        createUserInteractor: CreateUserInputPort,
        createTweetInteractor: CreateTweetInputPort,
        followInteractor: FollowInputPort
    ) = CommandController(createUserInteractor, createTweetInteractor, followInteractor)

    @Bean
    open fun queryController(
        viewTimelineInteractor: ViewTimelineInteractor
    ) = QueryController(viewTimelineInteractor)

    @Bean
    open fun userGateway() = UserGatewayInMemoryAdapter()

    @Bean
    open fun tweetGateway() = TweetGatewayInMemoryAdapter()

    @Bean
    open fun followGateway() = FollowGatewayInMemoryAdapter()

    @Bean
    open fun timelineGateway() = TimelineGatewayInMemoryAdapter()

    @Bean
    open fun createUserPresenter(view: ResponseView<Any>) = CreateUserPresenter(view)

    @Bean
    open fun createTweetPresenter(view: ResponseView<Any>) = CreateTweetPresenter(view)

    @Bean
    open fun followPresenter(view: ResponseView<Any>) = FollowPresenter(view)

    @Bean
    open fun viewTimelinePresenter(view: ResponseView<Any>) = ViewTimelinePresenter(view)

    @Bean
    @Scope(
        value = WebApplicationContext.SCOPE_REQUEST,
        proxyMode = ScopedProxyMode.TARGET_CLASS
    )
    open fun responseView() = ResponseHandlerView()
}