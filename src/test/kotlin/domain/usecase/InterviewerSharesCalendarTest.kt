package domain.usecase

import arrow.core.Either
import domain.calendar.Calendar
import domain.slot.Free
import domain.slot.Taken
import domain.usecase.InterviewerShareCalendar.Deny
import domain.usecase.InterviewerShareCalendar.Request
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class InterviewerSharesCalendarTest {
    private val candidate = "candidate@gmail.com"
    private val another = "another@gmail.com"
    private val interviewer = "interviewer@gmail.com"
    private val at = LocalDateTime.now();
    private val spans = 15

    @Test
    fun `Interviewer should Share its Calendar to Candidate`() {
        val rep: InterviewerRepository = mockk(relaxed = true)
        val tok: InviteToken = mockk(relaxed = true)
        every { rep.getInterviewerCalendar(interviewer) } returns Calendar(
            Free(at, spans, interviewer)
        )
        when (val result = InterviewerShareCalendar(rep, tok).execute(Request(interviewer, candidate))) {
            is Either.Right -> {
                val (token, invited) = result.b
                assertNotNull(token)
                assertEquals(invited, candidate)
                verify(exactly = 1) { tok.createFor(interviewer, candidate) }
            }
        }
    }

    @Test
    fun `Interviewer should get and Deny if it has not Free Slots`() {
        val rep: InterviewerRepository = mockk(relaxed = true);
        every { rep.getInterviewerCalendar(interviewer) } returns Calendar(
            Taken(Free(at, spans, interviewer), another)
        )
        val tok: InviteToken = mockk(relaxed = true);
        when (val result = InterviewerShareCalendar(rep, tok).execute(Request(interviewer, candidate))) {
            is Either.Right -> fail()
            is Either.Left -> when (result.a){
                is Deny -> assertEquals(result.a.message, "No Free Slots")
            }
        }
    }
}