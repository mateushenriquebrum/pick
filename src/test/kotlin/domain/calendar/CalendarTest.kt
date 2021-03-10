package domain.calendar

import arrow.core.Either
import domain.slot.Free
import domain.slot.Taken
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CalendarTest {
    private var at = LocalDateTime.now();
    @Test
    fun `Calendar can be created with Free and Taken slots`() {
        val fstSlot = Free(at, 10, "")
        val sndSlot = Taken(Free(at.plusMinutes(10), 10, ""),"");
        val trdSlot = Free(at.plusMinutes(20), 10, "")
        var calendar = Calendar(sndSlot, trdSlot, fstSlot)
        assertEquals(calendar.slots.map { at }, listOf(fstSlot, sndSlot, trdSlot).map { at })
    }

    @Test
    fun `Calendar should not accept intersectional Free slot`() {
        val fstSlot = Free(at, 10, "")
        val sndSlot = Free(at, 10, "")
        var calendar = Calendar(fstSlot).add(sndSlot)
        when (calendar) {
            is Either.Left -> when (calendar.a) {
                is Error.NotAllowed -> assertTrue(true)
            }
            is Either.Right -> assertTrue(false)
        }
    }

    @Test
    fun `Calendar should accept non intersectional Free slot`() {
        val fstSlot = Free(at, 10, "")
        val sndSlot = Free(at.plusMinutes(10), 10, "")
        var calendar = Calendar(fstSlot).add(sndSlot)
        when (calendar) {
            is Either.Left -> assertTrue(true)
            is Either.Right -> when (calendar.b) {
                is Calendar -> assertEquals(calendar.b.slots.size, 2)
            }
        }
    }
}