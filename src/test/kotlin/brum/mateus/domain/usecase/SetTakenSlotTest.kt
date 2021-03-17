package brum.mateus.domain.usecase

import arrow.core.Either
import brum.mateus.domain.slot.Free
import brum.mateus.domain.slot.SlotId.SomeSlotId
import brum.mateus.domain.usecase.SetTakenSlot.Request
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class SetTakenSlotTest {
    @Test
    fun `Invited should Take a Free Slot`() {
        val id = SomeSlotId("any-slot-id")
        val token = "any-token"
        val candidate = "candidate@gmail.com"
        val rep: InterviewerRepository = mockk(relaxed = true)
        val now = LocalDateTime.now()
        val spans = 10L
        val interviewer = "interviewer@gmail.com"

        every { rep.getFreeSlotsById(any()) } returns Free(id, now, spans, interviewer)

        when(val result = SetTakenSlot(rep).execute(Request(id.data, token, candidate))) {
            is Either.Left -> fail("Shouldn't happen")
            is Either.Right -> {
                assertThat(result.b)
                    .isEqualTo( SetTakenSlot.Response.Success(
                        now.toString(),
                        now.plusMinutes(spans).toString(),
                        interviewer
                    ))
            }
        }
    }
}