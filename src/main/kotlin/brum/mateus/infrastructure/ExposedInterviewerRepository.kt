package brum.mateus.infrastructure

import brum.mateus.domain.calendar.Calendar
import brum.mateus.domain.slot.Free
import brum.mateus.domain.slot.Taken
import brum.mateus.domain.usecase.InterviewerRepository
import brum.mateus.domain.usecase.Token
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExposedInterviewerRepository : InterviewerRepository {

    object TableSlots : Table("slots") {
        val id = varchar("id", 128).clientDefault { UUID.randomUUID().toString() }
        val at = datetime("from")
        val spans = long("spans")
        val interviewer = varchar("interviewer", 50)
        val interviewee = varchar("interviewee", 50).nullable()
        val token = varchar("token", 50).nullable()
    }

    override fun setFreeSlot(free: Free): Unit = transaction {
        TableSlots.insert {
            it[TableSlots.at] = free.at
            it[TableSlots.spans] = free.spans
            it[TableSlots.interviewer] = free.interviewer
        }
    }

    override fun getInterviewerCalendar(interviewer: String): Calendar = transaction {
        TableSlots.select {
            TableSlots.interviewer eq interviewer
        }.map {
            if (it[TableSlots.interviewee].isNullOrEmpty()) {
                Free(it[TableSlots.at], it[TableSlots.spans], it[TableSlots.interviewer])
            } else {
                Taken(it[TableSlots.at], it[TableSlots.spans], it[TableSlots.interviewer], it[TableSlots.interviewee].toString())
            }
        }.let {
            Calendar(*it.toTypedArray())
        }
    }

    override fun setInvitationForCandidate(token: Token, candidate: String, slots: Set<Free>): Unit = transaction {
        TableSlots.update({ TableSlots.interviewer inList slots.map { it.interviewer } }) {
            it[TableSlots.token] = token.data
            it[TableSlots.interviewee] = candidate
        }
    }

    override fun getFreeSlotsByToken(token: Token): Set<Free> = transaction {
        TableSlots.select { TableSlots.token eq token.data }
            .map {
                Free(it[TableSlots.at], it[TableSlots.spans], it[TableSlots.interviewer])
            }
            .toSet()
    }

    override fun getFreeSlotsById(slotId: String): Free = transaction{
        TableSlots.select { TableSlots.id eq slotId }
            .map {
                Free(it[TableSlots.at], it[TableSlots.spans], it[TableSlots.interviewer])
            }.first()
    }

    override fun setTakenSlotForCandidate(taken: Taken): Unit = transaction {
        TableSlots.update ({TableSlots.id eq ""}) {
            it[TableSlots.interviewer] = taken.by
        }
    }
}