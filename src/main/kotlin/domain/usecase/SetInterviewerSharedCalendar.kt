package domain.usecase

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right

class SetInterviewerSharedCalendar(private val rep: InterviewerRepository, private val tok: InviteToken) {

    data class Request(val interviewer: String, val candidate: String)
    sealed class Response {
        data class Fail(val reason: String)
        data class Success(val token: String, val invited: String)
    }

    fun execute(request: Request): Either<Response.Fail, Response.Success> {
        val (interviewer, candidate) = request
        val token = tok.createFor(interviewer, candidate)
        val calendar = rep.getInterviewerCalendar(interviewer)

        return when (calendar.invite(candidate)) {
            is Left -> Left(Response.Fail("No Free Slots"))
            is Right -> {
                rep.setInvitationForCandidate(token, candidate, calendar.free)
                Right(Response.Success(token.data, candidate))
            }
        }
    }
}
