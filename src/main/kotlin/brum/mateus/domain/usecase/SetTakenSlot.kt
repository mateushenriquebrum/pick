package brum.mateus.domain.usecase

import arrow.core.Either
import arrow.core.Right
import brum.mateus.domain.slot.SlotId
import brum.mateus.domain.slot.SlotId.SomeSlotId

class SetTakenSlot(private val rep: InterviewerRepository) {
    data class Request(val slotId: String, val token: String, val candidate: String)

    sealed class Response {
        data class Fail(val reason: String)
        data class Success(val from: String, val to: String, val interviewer: String)
    }

    fun execute(request: Request): Either<Response.Fail, Response.Success> {
        val free = rep.getFreeSlotsById(SomeSlotId(request.slotId))
        val taken = free!!.takenBy(request.candidate)
        rep.setTakenSlotForCandidate(taken)
        val result = taken.let {
            Response.Success(
                it.at.toString(),
                it.at.plusMinutes(it.spans).toString(),
                it.interviewer
            )
        }
        return Right(result)
        // exception:
        //// id do not exists
        //// slot is not free
        //// token is not from that slot
    }
}
