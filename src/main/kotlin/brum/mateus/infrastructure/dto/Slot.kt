package brum.mateus.infrastructure.dto

import kotlinx.serialization.Serializable

@Serializable
data class Slot(val from: String, val to: String, val interviewer: String, val candidate: String)