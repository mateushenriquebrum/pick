package brum.mateus.domain.usecase

import arrow.core.Either
import brum.mateus.domain.slot.Free
import brum.mateus.domain.slot.SlotId
import brum.mateus.domain.slot.SlotId.NewSlotId
import java.time.LocalDateTime

class SetInterviewerFreeSlots(private val rep: InterviewerRepository) {

    sealed class Response {
        data class Fail(val reason: String)
        data class Success(val interviewer: String, val from: String, val to: String)
    }

    fun execute(interviewer: String, at: LocalDateTime, spans: Long): Either<Response.Fail, Response.Success> {
        val free = Free(NewSlotId(), at, spans, interviewer)
        val calendar = rep.getInterviewerCalendar(interviewer)
        return calendar.add(free).bimap(
            {
                Response.Fail("Slot already set")
            },
            {
                rep.setFreeSlot(free)
                Response.Success(interviewer, at.toString(), at.plusMinutes(spans).toString())
            }
        )
    }
}