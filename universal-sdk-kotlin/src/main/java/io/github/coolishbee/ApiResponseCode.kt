package io.github.coolishbee

enum class ApiResponseCode(val code: Int) {
    /**
     * The request was successful.
     */
    SUCCESS(5000),
    /**
     * The request was successful.
     */
    FAILED(5001);


    companion object {
        fun fromInt(value: Int) = values().first{
            it.code == value
        }
    }
}