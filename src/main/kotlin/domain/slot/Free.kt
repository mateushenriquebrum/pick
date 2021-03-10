package domain.slot

import java.time.LocalDateTime

class Free(at: LocalDateTime,spans: Int, private val interviewer: String): Slot(at, spans) {
    fun takenBy(interviewee: String) = Taken(this, interviewee)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Free

        if (interviewer != other.interviewer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + interviewer.hashCode()
        return result
    }

}
