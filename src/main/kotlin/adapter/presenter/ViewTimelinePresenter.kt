package org.personal.adapter.presenter

import org.personal.adapter.view.ResponseView
import org.personal.adapter.view.ResponseViewModel
import org.personal.adapter.view.ResponseViewModel.InternalServerError
import org.personal.adapter.view.TweetDto
import org.personal.application.usecase.viewtimeline.ViewTimelineOutputPort
import org.personal.application.usecase.viewtimeline.ViewTimelineResponseModel

class ViewTimelinePresenter(
    private val view: ResponseView<Any>
) : ViewTimelineOutputPort {
    override fun presentViewTimeline(responseModel: ViewTimelineResponseModel) {
        val viewModel = when (responseModel) {
            is ViewTimelineResponseModel.Success -> ResponseViewModel.SuccessRetrieve(mapToTweetDtoList(responseModel))
            is ViewTimelineResponseModel.Failure.ValidationError -> ResponseViewModel.BadRequest(responseModel.message)
            is ViewTimelineResponseModel.Failure.NotFoundError -> ResponseViewModel.NotFound(responseModel.message)
            is ViewTimelineResponseModel.Failure.UnexpectedError -> InternalServerError(responseModel.message)
        }
        view.displayResult(viewModel)
    }

    private fun mapToTweetDtoList(responseModel: ViewTimelineResponseModel.Success) = responseModel.tweets.map {
        TweetDto(
            id = it.id.toString(),
            authorId = it.authorId.toString(),
            content = it.content,
            createdAt = it.createdAt.toString().substringBeforeLast(".")
        )
    }
}