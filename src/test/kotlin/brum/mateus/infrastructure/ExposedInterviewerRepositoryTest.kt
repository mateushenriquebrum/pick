package brum.mateus.infrastructure

import brum.mateus.domain.slot.Free
import brum.mateus.infrastructure.ExposedInterviewerRepository.TableSlots
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ExposedInterviewerRepositoryTest {

    val rep = ExposedInterviewerRepository()
    val at = LocalDateTime.now()
    val spans = 10L
    val interviewer = "mateus@gmail.com"

    @BeforeEach
    fun before() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "org.h2.Driver")
        transaction {
            SchemaUtils.create(TableSlots)
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
            rep.setFreeSlot(Free(at, spans, interviewer))
            assertThat(TableSlots.selectAll()).hasSize(1)
        }
    }
}