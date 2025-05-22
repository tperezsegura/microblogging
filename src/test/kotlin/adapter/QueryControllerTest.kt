package adapter

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.personal.adapter.controller.QueryController
import org.personal.application.usecase.viewtimeline.ViewTimelineInputPort
import org.personal.application.usecase.viewtimeline.ViewTimelineRequestModel

class QueryControllerTest {
    private lateinit var viewTimeline: ViewTimelineInputPort
    private lateinit var controller: QueryController

    @BeforeEach
    fun setUp() {
        viewTimeline = mockk(relaxed = true)
        controller = QueryController(viewTimeline)
    }

    @Test
    fun `given user id when view timeline then delegate to view timeline interactor with request model`() {
        assertDoesNotThrow { controller.viewTimeline(42L) }
        verify { viewTimeline.viewTimeline(ViewTimelineRequestModel(42L)) }
    }
}