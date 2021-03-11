package domain.usecase

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import domain.slot.Free
import domain.usecase.GetInvitedSlots.Request
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class GetInvitedSlotsTest {

    private val token = "some"
    private val interviewer = "interviewer@gmail.com"
    private val at = LocalDateTime.now()
    private val spans = 15

    @Test @Disabled
    fun `Candidate should get Interviewer's Slots by Token`() {
        when (val result = GetInvitedSlots().execute(Request(token))) {
            is Left -> Assertions.fail()
            is Right -> Assertions.assertEquals(listOf(Free(at, spans, interviewer)), result.b.slots)
        }
    }
}