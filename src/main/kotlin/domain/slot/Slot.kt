package domain.slot

import java.time.LocalDateTime

open class Slot(val at: LocalDateTime, val spans: Long) {
    fun intersect(another: Slot) =
        (another.at.isAfter(at) &&
                another.at.isBefore(at.plusMinutes(spans))) ||
                another.at.isEqual(at)
}