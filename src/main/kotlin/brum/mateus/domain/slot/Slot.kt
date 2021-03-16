package brum.mateus.domain.slot

import java.time.LocalDateTime
import java.util.*


sealed class SlotId(val data: String) {
    class NewSlotId: SlotId(UUID.randomUUID().toString())
    class SomeSlotId(id: String): SlotId(id)
}

open class Slot(val id: SlotId = SlotId.NewSlotId(), val at: LocalDateTime, val spans: Long) {
    fun intersect(another: Slot) =
        (another.at.isAfter(at) &&
                another.at.isBefore(at.plusMinutes(spans))) ||
                another.at.isEqual(at)
}