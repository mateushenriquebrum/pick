package brum.mateus.infrastructure

import brum.mateus.domain.slot.Free
import brum.mateus.domain.slot.SlotId
import brum.mateus.domain.slot.SlotId.NewSlotId
import brum.mateus.domain.usecase.Token
import brum.mateus.infrastructure.ExposedInterviewerRepository.TableSlots
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Ignore
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ExposedInterviewerRepositoryTest {

    private val rep = ExposedInterviewerRepository()
    private val at = LocalDateTime.now()
    private val spans = 10L
    private val interviewer = "mateus@gmail.com"

    @BeforeEach
    fun before() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "org.h2.Driver")
        transaction {
            SchemaUtils.create(TableSlots)
        }
    }

    private fun fixture() = transaction {
        TableSlots.insert {
            it[TableSlots.id] = "without-candidate"
            it[TableSlots.at] = LocalDateTime.now()
            it[TableSlots.spans] = 10
            it[TableSlots.interviewer] = "int@gmail.com"
        }
        TableSlots.insert {
            it[TableSlots.id] = "with-candidate"
            it[TableSlots.at] = LocalDateTime.now()
            it[TableSlots.spans] = 10
            it[TableSlots.interviewer] = "int@gmail.com"
            it[TableSlots.interviewee] = "can@gmail.com"
        }
    }

    @AfterEach
    fun after() {
        transaction {
            SchemaUtils.drop(TableSlots)
        }
    }

    @Test
    fun `Should insert a Free Slot`() {
        transaction {
            rep.setFreeSlot(Free(NewSlotId(), at, spans, interviewer))
            assertThat(TableSlots.selectAll()).hasSize(1)
        }
    }

    @Test
    fun `Should fetch just Free Slots`() {
        transaction {
            fixture()
            assertThat(rep.getFreeSlotsById("without-candidate")).isNotNull
            assertThat(rep.getFreeSlotsById("with-candidate")).isNull()
        }
    }

    @Test
    fun `Should fetch Interviewer Calendar`() {
        transaction {
            fixture()
            assertThat(rep.getInterviewerCalendar("int@gmail.com").slots).hasSize(2)
        }
    }

    @Ignore
    fun `Should set an Slot as Free`() {
        transaction {
            fixture()
            val free = rep.getFreeSlotsById("without-candidate") as Free
            rep.setInvitationForCandidate(Token("123"), "new.candidate@gmail.com", setOf(free))
            assertThat(TableSlots.select { TableSlots.interviewee eq "new.candidate@gmail.com" }).hasSize(1)
        }
    }
}