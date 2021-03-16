package brum.mateus.domain.slot

import java.time.LocalDateTime

class Taken(id: SlotId, at: LocalDateTime, spans: Long, val interviewer: String, val by: String) : Slot(id, at, spans)
