package domain.usecase

import arrow.core.Either
import domain.calendar.Calendar
import domain.slot.Free
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.`in`
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.now

class SetInterviewerFreeSlotsTest {
    private val now = now()
    private val interviewer = "interviewer@gmail.com"

    @Test
    fun `Interviewer should get a Confirmation when set a Free Slot on its Calendar`() {
        val rep: InterviewerRepository = mockk(relaxed = true);
        every { rep.getInterviewerCalendar(any()) } returns Calendar()
        when (val result = SetInterviewerFreeSlots(rep).execute(interviewer, now, 10)) {
            is Either.Right -> {
                assertThat(result.b)
                    .hasFieldOrPropertyWithValue("interviewer", interviewer)
                    .hasFieldOrPropertyWithValue("from", now.toString())
                    .hasFieldOrPropertyWithValue("to", now.plusMinutes(10).toString())
                verify { rep.setFreeSlot(Free(now, 10, interviewer)) }
            }
            is Either.Left -> fail()
        }
    }

    @Test
    fun `Interviewer should get a Deny when set a existent Free Slot on its Calendar`() {
        val rep: InterviewerRepository = mockk(relaxed = true);
        every { rep.getInterviewerCalendar(any()) } returns Calendar(Free(now, 10, interviewer))
        when (val result = SetInterviewerFreeSlots(rep).execute(interviewer, now, 10)) {
            is Either.Right -> fail()
            is Either.Left -> {
                assertThat(result.a.reason)
                    .isEqualToIgnoringCase("Slot already set")
                verify(exactly = 0) { rep.setFreeSlot(any()) }
            }
        }
    }
}