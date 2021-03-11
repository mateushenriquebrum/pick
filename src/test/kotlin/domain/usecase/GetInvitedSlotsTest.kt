package domain.usecase

import arrow.core.Either.Left
import arrow.core.Either.Right
import domain.slot.Free
import domain.usecase.GetInvitedSlots.Request
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class GetInvitedSlotsTest {

    private val token = "some"
    private val interviewer = "interviewer@gmail.com"
    private val at = LocalDateTime.now()
    private val spans = 15L

    @Test
    fun `Candidate should get Interviewer's Slots by Token`() {
        val rep: InterviewerRepository = mockk(relaxed = true)
        every { rep.getFreeSlotsByToken(Token(token)) } returns setOf(
            Free(at, spans, interviewer)
        )
        when (val result = GetInvitedSlots(rep).execute(Request(token))) {
            is Left -> fail()
            is Right -> {
                assertEquals(interviewer, result.b.interviewer)
                assertEquals(1, result.b.slots.size)
            }
        }
    }
}