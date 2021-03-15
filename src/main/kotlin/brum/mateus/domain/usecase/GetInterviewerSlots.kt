package brum.mateus.domain.usecase

import arrow.core.Either
import arrow.core.Either.Companion.right

class GetInterviewerSlots(private val rep: InterviewerRepository) {
    data class Request(val interviewer: String)
    data class Slot(val from: String, val to: String)

    sealed class Response {
        data class Fail(val reason: String)
        data class Success(val interviewer: String, val slots: Set<Slot>)
    }

    fun execute(request: Request): Either<Response.Fail, Response.Success> {
        return rep
            .getInterviewerCalendar(request.interviewer)
            .slots
            .map { Slot(it.at.toString(), it.at.plusMinutes(it.spans).toString()) }
            .toSet()
            .let { right(Response.Success(request.interviewer, it)) }
    }
}
