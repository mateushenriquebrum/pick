package domain.usecase

import arrow.core.Either.Left
import arrow.core.Either.Right
import domain.calendar.Calendar
import domain.slot.Free
import domain.slot.Taken
import domain.usecase.SetInterviewerSharedCalendar.Request
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class SetInterviewerSharedCalendarTest {
    private val candidate = "candidate@gmail.com"
    private val another = "another@gmail.com"
    private val interviewer = "interviewer@gmail.com"
    private val at = LocalDateTime.now()
    private val spans = 15L
    private val token = Token("token")

    @Test
    fun `Interviewer should Share its Calendar by Token to a Candidate`() {
        val rep: InterviewerRepository = mockk(relaxed = true)
        val tok: InviteToken = mockk(relaxed = true)
        every { tok.createFor(interviewer, candidate) } returns token
        every { rep.getInterviewerCalendar(interviewer) } returns Calendar(
            Free(at, spans, interviewer)
        )
        when (val result = SetInterviewerSharedCalendar(rep, tok).execute(Request(interviewer, candidate))) {
            is Right -> {
                val (token, invited) = result.b
                assertNotNull(token)
                assertNotNull(invited)
                verify(exactly = 1) { tok.createFor(interviewer, candidate) }
                verify(exactly = 1) {
                    rep.setInvitationForCandidate(
                        any<Token>(),
                        any<String>(),
                        any<Set<Free>>()
                    )
                }
            }
        }
    }

    @Test
    fun `Interviewer should get and Deny if it has not Free Slots`() {
        val rep: InterviewerRepository = mockk(relaxed = true)
        every { rep.getInterviewerCalendar(interviewer) } returns Calendar(
            Taken(at, spans, interviewer, another)
        )
        val tok: InviteToken = mockk(relaxed = true)
        when (val result = SetInterviewerSharedCalendar(rep, tok).execute(Request(interviewer, candidate))) {
            is Right -> fail()
            is Left -> assertEquals(result.a.reason, "No Free Slots")
        }
    }
}