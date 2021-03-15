package brum.mateus.domain.usecase

import arrow.core.Either
import brum.mateus.domain.slot.Free
import brum.mateus.domain.usecase.SetInvitedTakeSlot.Request
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class SetInvitedTakeSlotTest {
    @Test
    fun `Invited should Take a Free Slot`() {
        val id = "any-slot-id"
        val token = "any-token"
        val candidate = "candidate@gmail.com"
        val rep: InterviewerRepository = mockk(relaxed = true)
        val now = LocalDateTime.now()
        val spans = 10L
        val interviewer = "interviewer@gmail.com"

        every { rep.getFreeSlotsById(id) } returns Free(now, spans, interviewer)

        when(val result = SetInvitedTakeSlot(rep).execute(Request(id, token, candidate))) {
            is Either.Left -> fail("Shouldn't happen")
            is Either.Right -> {
                assertThat(result.b)
                    .isEqualTo( SetInvitedTakeSlot.Response.Success(
                        now.toString(),
                        now.plusMinutes(spans).toString(),
                        interviewer
                    ))
            }
        }
    }
}