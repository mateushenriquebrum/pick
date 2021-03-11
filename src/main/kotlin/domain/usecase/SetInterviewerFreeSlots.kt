package domain.usecase

import arrow.core.Either
import domain.slot.Free
import java.time.LocalDateTime

class SetInterviewerFreeSlots(private val rep: InterviewerRepository) {
    data class Deny(val reason: String)
    data class Confirm(val interviewer: String, val at: LocalDateTime, val spans: Int)
    fun execute(interviewer: String, at: LocalDateTime, spans: Int): Either<Deny, Confirm> {
        val free = Free(at, spans, interviewer)
        val calendar = rep.getInterviewerCalendar(interviewer)
        return calendar.add(free).bimap(
            {
                Deny("Slot already set")
            },
            {
                rep.setFreeSlot(free)
                Confirm(interviewer, at, spans)
            }
        )
    }
}