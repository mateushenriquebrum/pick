package domain.slot

import java.time.LocalDateTime

open class Slot(val at: LocalDateTime, val spans: Int) {
    fun intersect(another: Slot) =
        (another.at.isAfter(at) &&
        another.at.isBefore(at.plusMinutes(spans.toLong()))) ||
        another.at.isEqual(at)
}