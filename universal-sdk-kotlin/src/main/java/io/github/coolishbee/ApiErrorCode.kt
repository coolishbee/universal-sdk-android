package io.github.coolishbee

enum class ApiErrorCode(val code: Int) {
    NOT_DEFINED(-1),
    CANCEL(7001),
    AUTHENTICATION_AGENT_ERROR(7002),
    INTERNAL_ERROR(7003);

    companion object {
        fun fromInt(value: Int) = values().first{
            it.code == value
        }
    }
}