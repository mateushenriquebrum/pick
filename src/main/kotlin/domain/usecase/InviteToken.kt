package domain.usecase

interface InviteToken {
    data class Token(val data: String)

    fun createFor(interviewer: String, candidate: String): Token
}
