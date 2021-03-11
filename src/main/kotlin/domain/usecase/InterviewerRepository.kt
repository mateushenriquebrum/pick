package domain.usecase

import domain.calendar.Calendar
import domain.slot.Free

interface InterviewerRepository {
    fun setFreeSlot(free: Free)
    fun getInterviewerCalendar(interviewer: String): Calendar
    fun setInvitationForCandidate(token: Token, candidate: String, slots: Set<Free>)
    fun getFreeSlotsByToken(token: Token): Set<Free>
}
