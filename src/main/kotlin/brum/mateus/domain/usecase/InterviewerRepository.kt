package brum.mateus.domain.usecase

import brum.mateus.domain.calendar.Calendar
import brum.mateus.domain.slot.Free
import brum.mateus.domain.slot.Taken

interface InterviewerRepository {
    fun setFreeSlot(free: Free)
    fun getInterviewerCalendar(interviewer: String): Calendar
    fun setInvitationForCandidate(token: Token, candidate: String, slots: Set<Free>)
    fun getFreeSlotsByToken(token: Token): Set<Free>
    fun getFreeSlotsById(slotId: String): Free
    fun setTakenSlotForCandidate(taken: Taken)
}
