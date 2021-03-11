package domain.usecase

import arrow.core.Either
import arrow.core.Left

class GetInvitedSlots(private val rep: InterviewerRepository) {

    data class Slot(val from: String, val to: String)
    data class Response(val interviewer: String, val slots: Set<Slot>)
    data class Request(val token: String)
    class Error

    fun execute(request: Request): Either<Error, Response> {
        val (token) = request
        val free = rep.getFreeSlotsByToken(Token(token))
        return if (free.isEmpty()) Left(Error())
        else {
            val interviewer = free.map { it.interviewer }.first()
            Either.right(
                Response(
                    interviewer,
                    free.map { Slot(it.at.toString(), it.at.plusMinutes(it.spans).toString()) }.toSet()
                )
            )
        }
    }

}
