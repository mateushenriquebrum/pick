package brum.mateus.domain.slot

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FreeTest {

    private val at: LocalDateTime = LocalDateTime.now();
    private val someInterviewer = "some@gmail.com"
    private val anotherInterviewer = "another@gmail.com"

    @Test
    fun `A Free slot can be set to Taken by an interviewer`() {
        var interviewee = "interviewee@gmail.com";
        var free = Free(at, 15, someInterviewer);
        var taken = free.takenBy(interviewee);
        assertEquals(taken.by, interviewee)
    }

    @Test
    fun `A Free Slot cannot intersect with another Free Slot`() {
        var tenMinLater = at.plusMinutes(10);
        var fifteenMinLater = at.plusMinutes(15);
        var fstFree = Free(at, 15, someInterviewer);
        var sndFree = Free(tenMinLater, 15, someInterviewer);
        var trdFree = Free(fifteenMinLater, 15, someInterviewer);
        assertTrue(fstFree.intersect(fstFree))
        assertTrue(fstFree.intersect(sndFree))
        assertTrue(sndFree.intersect(trdFree))
        assertFalse(fstFree.intersect(trdFree))
    }
}