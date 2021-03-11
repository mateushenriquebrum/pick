package domain.usecase

import arrow.core.Either
import domain.usecase.InterviewerShareCalendar.Request
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class InterviewerSharesCalendarTest {
    private val candidate = "candidate@gmail.com"
    private val interviewer = "interviewer@gmail.com"

    @Test
    fun `Interviewer should Share its Calendar to Candidate`() {
        val rep: InterviewerRepository = mockk(relaxed = true);
        val tok: InviteToken = mockk(relaxed = true);
        when (val result = InterviewerShareCalendar(rep, tok).execute(Request(interviewer, candidate))) {
            is Either.Right -> {
                val (token, invited) = result.b
                assertNotNull(token)
                assertEquals(invited, candidate)
                verify(exactly = 1) { tok.createFor(interviewer, candidate) }
            }
        }
    }
}