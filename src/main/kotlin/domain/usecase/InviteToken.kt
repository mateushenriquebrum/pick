package domain.usecase

data class Token(val data: String)
interface InviteToken {
    fun createFor(interviewer: String, candidate: String): Token
}
