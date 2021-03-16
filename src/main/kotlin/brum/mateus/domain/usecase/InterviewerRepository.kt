package brum.mateus.domain.usecase

import arrow.core.OptionOf
import brum.mateus.domain.calendar.Calendar
import brum.mateus.domain.slot.Free
import brum.mateus.domain.slot.SlotId
import brum.mateus.domain.slot.Taken

interface InterviewerRepository {
    fun setFreeSlot(free: Free)
    fun getInterviewerCalendar(interviewer: String): Calendar
    fun setInvitationForCandidate(token: Token, candidate: String, slots: Set<SlotId>)
    fun getFreeSlotsByToken(token: Token): Set<Free>
    fun getFreeSlotsById(slotId: String): Free?
    fun setTakenSlotForCandidate(taken: Taken)
}
