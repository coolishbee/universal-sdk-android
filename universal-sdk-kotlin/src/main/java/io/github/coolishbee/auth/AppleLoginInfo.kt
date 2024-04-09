package io.github.coolishbee.auth

data class AppleLoginInfo(
    val iss: String,
    val aud: String,
    val exp: Int,
    val iat: Int,
    val sub: String,
    val nonce: String,
    val c_hash: String,
    val email: String,
    val email_verified: Boolean,
    val auth_time: Int,
    val nonce_supported: Boolean
)