package brum.mateus.domain.slot

import brum.mateus.domain.slot.SlotId.NewSlotId
import java.time.LocalDateTime

class Free(id: SlotId, at: LocalDateTime, spans: Long, val interviewer: String) : Slot(id, at, spans) {
    fun takenBy(interviewee: String) = Taken(NewSlotId(), this.at, this.spans, this.interviewer, interviewee)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Free

        if (interviewer != other.interviewer) return false

        return true
    }

    override fun hashCode(): Int {
        return interviewer.hashCode()
    }
}
