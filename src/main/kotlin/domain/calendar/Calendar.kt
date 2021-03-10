package domain.calendar

import arrow.core.Either
import domain.slot.Free
import domain.slot.Slot

sealed class Error {
    object NotAllowed : Error()
}

class Calendar(vararg slots: Slot) {
    fun add(toAdd: Free): Either<Error, Calendar> {
        return if (this.slots.any { toAdd.intersect(it) }) Either.left(Error.NotAllowed)
        else Either.right(Calendar(*(this.slots + toAdd).toTypedArray()))
    }

    val slots: List<Slot> = slots.sortedBy { free -> free.at.nano }
}
