package brum.mateus.domain.calendar

import arrow.core.Either
import brum.mateus.domain.slot.Free
import brum.mateus.domain.slot.Slot

class Calendar(vararg slots: Slot) {

    sealed class Response {
        object NotAllowed : Response()
        object Allowed : Response()
    }

    fun add(toAdd: Free): Either<Response, Calendar> {
        return if (this.slots.any { toAdd.intersect(it) }) Either.left(Response.NotAllowed)
        else Either.right(Calendar(*(this.slots + toAdd).toTypedArray()))
    }

    val free: Set<Free> = slots.filterIsInstance<Free>().toSet()

    fun invite(candidate: String): Either<Response.NotAllowed, Response.Allowed> {
        return if (free.isEmpty()) Either.left(Response.NotAllowed)
        else Either.right(Response.Allowed)
    }

    val slots: List<Slot> = slots.sortedBy { free -> free.at.nano }
}
