package domain.usecase

import arrow.core.Either
import domain.calendar.Calendar
import domain.slot.Free
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.now

class SetFreeSlotOnCalendarTest {
    @Test
    fun `Interviewer should get a Confirmation when set a Free Slot on its Calendar`() {
        val now = now()
        val rep:InterviewerRepository = mockk(relaxed = true);
        val setFree = SetFreeSlotCalendar(rep)
        var result = setFree.execute("interviewer@gmail.com", now, 10)
        when(result) {
            is Either.Right -> {
                assertEquals(result.b, Confirm("interviewer@gmail.com", now, 10))
                verify { rep.setFreeSlot("interviewer@gmail.com", Free(now, 10, "interviewer@gmail.com")) }
            }
            is Either.Left -> fail()
        }
    }

    @Test
    fun `Interviewer should get a Negation when set a existent Free Slot on its Calendar`() {
        val now = now()
        val interviewer = "interviewer@gmail.com"
        val rep:InterviewerRepository = mockk(relaxed = true);
        every { rep.getInterviewerCalendar(any()) } returns Calendar(Free(now, 10, interviewer))
        val setFree = SetFreeSlotCalendar(rep)
        var result = setFree.execute(interviewer, now, 10)
        when(result) {
            is Either.Right -> fail()
            is Either.Left -> {
                assertEquals(result.a, Invalid("Slot already set"))
                verify (exactly = 0) { rep.setFreeSlot(any(), any()) }
            }
        }
    }
}