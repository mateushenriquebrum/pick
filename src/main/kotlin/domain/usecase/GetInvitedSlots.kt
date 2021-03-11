package domain.usecase

import arrow.core.Either
import domain.slot.Free

class GetInvitedSlots {

    data class Slot (val from: String, val to: String)
    data class Result(val interviewer: String, val slots: Set<Slot>)
    data class Request(val token: String)

    fun execute(request: Request): Either<Nothing, Result> {
        return Either.right(Result("", setOf()))
    }

}
