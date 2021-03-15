package brum.mateus.domain.usecase

import arrow.core.Either
import brum.mateus.domain.calendar.Calendar
import brum.mateus.domain.slot.Free
import brum.mateus.domain.slot.Taken
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.*;

class GetInterviewerSlotsTest {
    private val now = LocalDateTime.now()
    private val after = now.plusMinutes(30)
    private val interviewer = "interviewer@gmail.com"
    private val by = "interviewee"
    private val spans: Long = 10
    @Test
    fun `Interviewer should get all its Slots`() {
        val rep: InterviewerRepository = mockk(relaxed = true);
        every { rep.getInterviewerCalendar(any()) } returns Calendar(
            Free(now, spans, interviewer),
            Taken(after, spans, interviewer, by)
        )
        when (val result = GetInterviewerSlots(rep).execute(GetInterviewerSlots.Request(interviewer))) {
            is Either.Left -> fail()
            is Either.Right -> {
                assertThat(result.b.interviewer)
                    .isEqualTo(interviewer)
                assertThat(result.b.slots)
                    .hasSize(2)
                    .contains(
                        GetInterviewerSlots.Slot(now.toString(),now.plusMinutes(spans).toString()),
                        GetInterviewerSlots.Slot(after.toString(),after.plusMinutes(spans).toString())
                    )
                verify(exactly = 1) { rep.getInterviewerCalendar(interviewer) }
            }
        }
    }
}