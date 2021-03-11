package domain.usecase

import arrow.core.Either

class SetInvitedTakeSlot(private val rep: InterviewerRepository) {
    data class Response(val from: String, val to: String, val interviewer: String)
    data class Request(val slotId: String, val token: String, val candidate: String)
    sealed class Deny
    fun execute(request: Request): Either<Deny, Response> {
        val free = rep.getFreeSlotsById(request.slotId)
        val taken = free.takenBy(request.candidate)
        rep.setTakenSlotForCandidate(taken)
        val result = taken.let {
            Response(
                it.at.toString(),
                it.at.plusMinutes(it.spans).toString(),
                it.interviewer
            )
        }
        return Either.right(result)
        // exception:
        //// id do not exists
        //// slot is not free
        //// token is not from that slot
    }
}
