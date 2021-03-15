package domain.usecase

import arrow.core.Either.Left
import arrow.core.Either.Right
import domain.calendar.Calendar
import domain.slot.Free
import domain.slot.Taken
import domain.usecase.SetInterviewerSharedCalendar.Request
import domain.usecase.SetInterviewerSharedCalendar.Response.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class SetInterviewerSharedCalendarTest {
    private val candidate = "candidate@gmail.com"
    private val another = "another@gmail.com"
    private val interviewer = "interviewer@gmail.com"
    private val at = LocalDateTime.now()
    private val spans = 15L
    private val token = Token("some-token")

    @Test
    fun `Interviewer should Share its Calendar by Token to a Candidate`() {
        val rep: InterviewerRepository = mockk(relaxed = true)
        val tok: InviteToken = mockk(relaxed = true)
        every { tok.createFor(interviewer, candidate) } returns token
        every { rep.getInterviewerCalendar(interviewer) } returns Calendar(
            Free(at, spans, interviewer)
        )
        when (val result = SetInterviewerSharedCalendar(rep, tok).execute(Request(interviewer, candidate))) {
            is Left -> fail("Shouldn't happen")
            is Right -> {
                assertThat(result.b)
                    .isEqualTo(Success("some-token", candidate))
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
    fun `Interviewer should get and Fail if it has not Free Slots`() {
        val rep: InterviewerRepository = mockk(relaxed = true)
        every { rep.getInterviewerCalendar(interviewer) } returns Calendar(
            Taken(at, spans, interviewer, another)
        )
        val tok: InviteToken = mockk(relaxed = true)
        when (val result = SetInterviewerSharedCalendar(rep, tok).execute(Request(interviewer, candidate))) {
            is Right -> fail("Shouldn't happen")
            is Left -> assertThat(result.a.reason)
                .isEqualToIgnoringCase("No Free Slots")
        }
    }
}