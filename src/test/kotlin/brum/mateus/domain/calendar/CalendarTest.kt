package brum.mateus.domain.calendar

import arrow.core.Either
import brum.mateus.domain.slot.Free
import brum.mateus.domain.slot.SlotId
import brum.mateus.domain.slot.SlotId.NewSlotId
import brum.mateus.domain.slot.Taken
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.time.LocalDateTime

class CalendarTest {
    private var at = LocalDateTime.now();

    @Test
    fun `Calendar can be created with Free and Taken slots`() {
        val fstSlot = Free(NewSlotId(), at, 10, "")
        val sndSlot = Taken(NewSlotId(), at.plusMinutes(10), 10, "", "");
        val trdSlot = Free(NewSlotId(), at.plusMinutes(20), 10, "")
        var calendar = Calendar(sndSlot, trdSlot, fstSlot)
        assertEquals(calendar.slots.map { at }, listOf(fstSlot, sndSlot, trdSlot).map { at })
    }

    @Test
    fun `Calendar should not accept intersectional Free slot`() {
        val fstSlot = Free(NewSlotId(), at, 10, "")
        val sndSlot = Free(NewSlotId(), at, 10, "")
        var calendar = Calendar(fstSlot).add(sndSlot)
        when (calendar) {
            is Either.Left -> when (calendar.a) {
                is Calendar.Response.NotAllowed -> assertTrue(true)
                else -> fail("Should not happen")
            }
            is Either.Right -> fail("Should not happen")
        }
    }

    @Test
    fun `Calendar should accept non intersectional Free slot`() {
        val fstSlot = Free(NewSlotId(), at, 10, "")
        val sndSlot = Free(NewSlotId(), at.plusMinutes(10), 10, "")
        var calendar = Calendar(fstSlot).add(sndSlot)
        when (calendar) {
            is Either.Left -> assertTrue(true)
            is Either.Right -> when (calendar.b) {
                is Calendar -> assertEquals(calendar.b.slots.size, 2)
            }
        }
    }
}