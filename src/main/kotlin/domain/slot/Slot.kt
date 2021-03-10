package domain.slot

import java.time.LocalDateTime

open class Slot(val at: LocalDateTime, val spans: Int) {
    fun intersect(another: Slot) =
        (another.at.isAfter(at) &&
        another.at.isBefore(at.plusMinutes(spans.toLong()))) ||
        another.at.isEqual(at)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Slot

        if (at != other.at) return false
        if (spans != other.spans) return false

        return true
    }

    override fun hashCode(): Int {
        var result = at.hashCode()
        result = 31 * result + spans
        return result
    }

}