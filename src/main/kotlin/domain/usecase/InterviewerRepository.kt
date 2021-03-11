package domain.usecase

import domain.calendar.Calendar
import domain.slot.Free

interface InterviewerRepository {
    fun setFreeSlot(free: Free)
    fun getInterviewerCalendar(interviewer: String): Calendar
}
