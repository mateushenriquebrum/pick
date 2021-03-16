package brum.mateus.domain.slot

import brum.mateus.domain.slot.SlotId.NewSlotId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TakenTest {
    @Test
    fun `A Taken slot should keep the Free slot properties`() {
        val free = Free(NewSlotId(), LocalDateTime.now(), 15, "int@gmail.com");
        val taken = free.takenBy("cand@gmail.com");
        assertThat(taken)
            .hasFieldOrPropertyWithValue("id", free.id)
            .hasFieldOrPropertyWithValue("at", free.at)
            .hasFieldOrPropertyWithValue("spans", free.spans)
            .hasFieldOrPropertyWithValue("interviewer", free.interviewer)
            .hasFieldOrPropertyWithValue("by", "cand@gmail.com")
    }
}