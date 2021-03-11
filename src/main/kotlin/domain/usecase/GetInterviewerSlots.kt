package domain.usecase

import arrow.core.Either
import arrow.core.Either.Companion.right
import java.time.LocalDateTime

class GetInterviewerSlots(private val rep: InterviewerRepository) {
    data class Response(val at: LocalDateTime, val spans: Int)
    data class Request(val interviewer: String)

    fun execute(request: Request): Either<Nothing, Set<Response>> {
        return rep
            .getInterviewerCalendar(request.interviewer)
            .slots
            .map { Response(it.at, it.spans) }
            .toSet()
            .let { right(it) }
    }
}
