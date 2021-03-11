package domain.usecase

import arrow.core.Either
import domain.calendar.Calendar
import domain.slot.Free
import domain.slot.Taken
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class GetInterviewerSlotsTest {
    private val now = LocalDateTime.now()
    private val after = now.plusMinutes(30)
    private val interviewer = "interviewer@gmail.com"
    private val by = "interviewee"

    @Test
    fun `Interviewer should get all its Slots`() {
        val rep: InterviewerRepository = mockk(relaxed = true);
        every { rep.getInterviewerCalendar(any()) } returns Calendar(
            Free(now, 10, interviewer),
            Taken(after, 10, interviewer, by)
        )
        when (val result = GetInterviewerSlots(rep).execute(GetInterviewerSlots.Request(interviewer))) {
            is Either.Left -> fail()
            is Either.Right -> {
                assertEquals(2, result.b.size)
                verify(exactly = 1) { rep.getInterviewerCalendar(interviewer) }
            }
        }
    }
}