package domain.usecase

import arrow.core.Either

class InterviewerShareCalendar(private val rep: InterviewerRepository, private val tok: InviteToken) {
    data class Request(val interviewer: String, val candidate: String)
    data class Response(val token: Token, val invited: String)

    fun execute(request: Request): Either<Nothing, Response> {
        val token = tok.createFor(request.interviewer, request.candidate)
        return Either.right(Response(token, request.candidate));
    }
}
