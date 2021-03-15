package brum.mateus.domain.usecase

import arrow.core.Either.Left
import arrow.core.Either.Right
import brum.mateus.domain.slot.Free
import brum.mateus.domain.usecase.GetInvitedSlots.Request
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class GetInvitedSlotsTest {

    private val token = "some"
    private val interviewer = "interviewer@gmail.com"
    private val now = LocalDateTime.now()
    private val spans = 15L

    @Test
    fun `Candidate should get Interviewer's Slots by Token`() {
        val rep: InterviewerRepository = mockk(relaxed = true)
        every { rep.getFreeSlotsByToken(Token(token)) } returns setOf(
            Free(now, spans, interviewer)
        )
        when (val result = GetInvitedSlots(rep).execute(Request(token))) {
            is Left -> fail()
            is Right -> {
                assertThat(result.b.interviewer)
                    .isEqualTo(interviewer)
                assertThat(result.b.slots)
                    .hasSize(1)
                    .contains(GetInvitedSlots.Slot(now.toString(), now.plusMinutes(spans).toString()))
            }
        }
    }
}