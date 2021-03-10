package domain.slot

class Taken(private val free: Free, public val by: String): Slot(free.at, free.spans)
