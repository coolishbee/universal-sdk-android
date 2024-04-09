package io.github.coolishbee.auth

data class AppleUser(
    val name: AppleName,
    val email: String
)
data class AppleName(
    val firstName: String,
    val lastName: String
)