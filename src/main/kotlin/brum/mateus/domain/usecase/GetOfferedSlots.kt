package brum.mateus.domain.usecase

import arrow.core.Either
import arrow.core.Left

class GetOfferedSlots(private val rep: InterviewerRepository) {

    data class Slot(val from: String, val to: String)
    data class Request(val token: String)
    sealed class Response {
        data class Fail(val reason: String)
        data class Success(val interviewer: String, val slots: Set<Slot>)
    }

    fun execute(request: Request): Either<Response.Fail, Response.Success> {
        val (token) = request
        val free = rep.getFreeSlotsByToken(Token(token))
        return if (free.isEmpty()) Left(Response.Fail("There is not any free slot"))
        else {
            val interviewer = free.map { it.interviewer }.first()
            Either.right(
                Response.Success(
                    interviewer,
                    free.map { Slot(it.at.toString(), it.at.plusMinutes(it.spans).toString()) }.toSet()
                )
            )
        }
    }

}
