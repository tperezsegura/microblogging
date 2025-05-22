package org.personal.infrastructure.web

import org.personal.adapter.controller.CommandController
import org.personal.adapter.controller.QueryController
import org.personal.adapter.view.ResponseView
import org.personal.adapter.view.ResponseViewModel
import org.personal.adapter.view.ResponseViewModel.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import java.util.concurrent.atomic.AtomicReference

@Configuration
open class Router {
    @Bean
    open fun userRoutes(
        commandController: CommandController,
        queryController: QueryController,
        responseHandler: ResponseHandlerView
    ): RouterFunction<ServerResponse> = router {
        POST("/users") { request ->
            val body = request.body<Map<String, Any>>()
            val username = body["username"] as? String
            commandController.createUser(username)
            buildResponse(responseHandler.getViewModel())
        }

        POST("/users/{id}/follow/{targetId}") { request ->
            val followerId = request.pathVariable("id").toLongOrNull()
            val followeeId = request.pathVariable("targetId").toLongOrNull()
            commandController.follow(followerId, followeeId)
            buildResponse(responseHandler.getViewModel())
        }

        GET("/users/{id}/timeline") { request ->
            val userId = request.pathVariable("id").toLongOrNull()
            queryController.viewTimeline(userId)
            buildResponse(responseHandler.getViewModel())
        }
    }

    @Bean
    open fun tweetRoutes(
        commandController: CommandController,
        responseHandler: ResponseHandlerView
    ): RouterFunction<ServerResponse> = router {
        POST("/tweets") { request ->
            val body = request.body<Map<String, Any>>()
            val authorId = (body["author_id"] as? Number)?.toLong()
            val content = body["content"] as? String
            commandController.createTweet(authorId, content)
            buildResponse(responseHandler.getViewModel())
        }
    }
}

private fun buildResponse(viewModel: ResponseViewModel<Any>) = when (viewModel) {
    is SuccessRetrieve -> buildSuccessRetrieveResponse(viewModel.data)
    is SuccessCreate -> buildSuccessCreateResponse(viewModel.data)
    is NoContent -> ServerResponse.noContent().build()
    is BadRequest -> buildErrorResponse(HttpStatus.BAD_REQUEST, viewModel.message)
    is NotFound -> buildErrorResponse(HttpStatus.NOT_FOUND, viewModel.message)
    is Conflict -> buildErrorResponse(HttpStatus.CONFLICT, viewModel.message)
    is InternalServerError -> buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, viewModel.message)
}

private fun buildSuccessRetrieveResponse(body: Any) =
    ServerResponse.status(HttpStatus.OK)
        .contentType(APPLICATION_JSON)
        .body(body)

private fun buildSuccessCreateResponse(body: Any) =
    ServerResponse.status(HttpStatus.CREATED)
        .contentType(APPLICATION_JSON)
        .body(body)

private fun buildErrorResponse(errorStatus: HttpStatus, message: String) =
    ServerResponse.status(errorStatus)
        .contentType(APPLICATION_JSON)
        .body(mapOf("message" to message))

open class ResponseHandlerView : ResponseView<Any> {
    private val responseViewModel = AtomicReference<ResponseViewModel<Any>>()

    override fun displayResult(viewModel: ResponseViewModel<Any>) = responseViewModel.set(viewModel)

    open fun getViewModel(): ResponseViewModel<Any> = responseViewModel.get()
}