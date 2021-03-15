package brum.mateus.infrastructure

import brum.mateus.infrastructure.dto.Slot
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        install(CallLogging)
        json()
    }
    routing {
        route("/interviewer") {
            get("{id}")  {
                val id = call.parameters["id"] ?: return@get call.respondText(
                    "Missing id ..",
                    status = HttpStatusCode.BadRequest
                )
                call.respond(Slot("10/10/2040", "10/10/2040", "mateus", "iago"))
            }
            post("{id}") {

            }
        }

    }
}