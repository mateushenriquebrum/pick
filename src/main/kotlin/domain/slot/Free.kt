package domain.slot

import java.time.LocalDateTime

class Free(at: LocalDateTime, spans: Long, val interviewer: String) : Slot(at, spans) {
    fun takenBy(interviewee: String) = Taken(this.at, this.spans, this.interviewer, interviewee)
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
