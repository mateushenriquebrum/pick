package brum.mateus.domain.usecase

import arrow.core.Either
import brum.mateus.domain.usecase.SetInterviewerSharedCalendar.Response.Fail
import brum.mateus.domain.usecase.SetInterviewerSharedCalendar.Response.Success

class SetInterviewerSharedCalendar(private val rep: InterviewerRepository, private val tok: InviteToken) {

    data class Request(val interviewer: String, val candidate: String)
    sealed class Response {
        data class Fail(val reason: String)
        data class Success(val token: String, val invited: String)
    }

    fun execute(request: Request): Either<Fail, Success> {
        val (interviewer, candidate) = request
        val token = tok.createFor(interviewer, candidate)
        val calendar = rep.getInterviewerCalendar(interviewer)

        return calendar.invite(candidate).bimap(
            {
                Fail("No Free Slots")
            },
            {
                rep.setInvitationForCandidate(token, candidate, calendar.free)
                Success(token.data, candidate)
            }
        )
    }
}
