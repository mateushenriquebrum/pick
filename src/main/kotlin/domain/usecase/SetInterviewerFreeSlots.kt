package domain.usecase

import arrow.core.Either
import domain.slot.Free
import java.time.LocalDateTime

class SetInterviewerFreeSlots(private val rep: InterviewerRepository) {

    sealed class Response {
        data class Fail(val reason: String)
        data class Success(val interviewer: String, val from: String, val to: String)
    }

    fun execute(interviewer: String, at: LocalDateTime, spans: Long): Either<Response.Fail, Response.Success> {
        val free = Free(at, spans, interviewer)
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