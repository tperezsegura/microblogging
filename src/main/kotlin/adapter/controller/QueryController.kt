package org.personal.adapter.controller

import org.personal.application.usecase.viewtimeline.ViewTimelineInputPort
import org.personal.application.usecase.viewtimeline.ViewTimelineRequestModel

class QueryController(
    private val viewTimelineInteractor: ViewTimelineInputPort
) {
    fun viewTimeline(userId: Long?) = viewTimelineInteractor.viewTimeline(
        ViewTimelineRequestModel(userId)
    )
}