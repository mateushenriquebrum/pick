package brum.mateus.domain.slot

import java.time.LocalDateTime

class Taken(at: LocalDateTime, spans: Long, val interviewer: String, val by: String) : Slot(at, spans)
