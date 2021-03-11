package domain.usecase

import arrow.core.Either
import domain.slot.Free
import domain.usecase.SetInvitedTakeSlot.Request
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class SetInvitedTakeSlotTest {
    @Test
    fun `Invited should Take a Free Slot`() {
        val id = "any-slot-id"
        val token = "any-token"
        val candidate = "candidate@gmail.com"
        val rep: InterviewerRepository = mockk(relaxed = true)
        var now = LocalDateTime.now()
        var spans = 10L
        var interviewer = "interviewer@gmail.com"

        every { rep.getFreeSlotsById(id) } returns Free(now, spans, interviewer)

        when(val result = SetInvitedTakeSlot(rep).execute(Request(id, token, candidate))) {
            is Either.Left -> fail()
            is Either.Right -> {
                val (from, to, interviewer) = result.b
                assertFalse(from.isNullOrEmpty())
                assertFalse(to.isNullOrEmpty())
                assertFalse(interviewer.isNullOrEmpty())
            }
        }
    }
}