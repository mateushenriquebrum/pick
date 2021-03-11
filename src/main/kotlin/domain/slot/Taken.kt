package domain.slot

class Taken(private val free: Free, val by: String) : Slot(free.at, free.spans)
