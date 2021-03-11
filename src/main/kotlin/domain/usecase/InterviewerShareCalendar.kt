package domain.usecase

import arrow.core.Either
import arrow.core.Either.Companion.left
import arrow.core.Either.Companion.right
import arrow.core.Either.Left
import arrow.core.Either.Right
import domain.calendar.Calendar

class InterviewerShareCalendar(private val rep: InterviewerRepository, private val tok: InviteToken) {

    data class Request(val interviewer: String, val candidate: String)
    data class Response(val token: Token, val invited: String)
    data class Deny(val message: String)

    fun execute(request: Request): Either<Deny, Response> {
        val (interviewer, candidate) = request
        val token = tok.createFor(interviewer, candidate)
        val calendar = rep.getInterviewerCalendar(interviewer)

        return when (calendar.invite(candidate)) {
            is Left -> Left(Deny("No Free Slots"))
            is Right -> Right(Response(token, candidate))
        }
    }
}
