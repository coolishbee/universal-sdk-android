package io.github.coolishbee.utils

import android.os.Build
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.CodingErrorAction
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

class UniversalUtils {

    companion object {
        fun md5(input:String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }

        fun sha256(s: String): String {
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(s.toByteArray())
            val hash = StringBuilder()
            for (c in digest) {
                hash.append(String.format("%02x", c))
            }
            return hash.toString()
        }

        fun generateNonce(length: Int): String {
            val generator = SecureRandom()

            val charsetDecoder = StandardCharsets.US_ASCII.newDecoder()
            charsetDecoder.onUnmappableCharacter(CodingErrorAction.IGNORE)
            charsetDecoder.onMalformedInput(CodingErrorAction.IGNORE)

            val bytes = ByteArray(length)
            val inBuffer = ByteBuffer.wrap(bytes)
            val outBuffer = CharBuffer.allocate(length)
            while (outBuffer.hasRemaining()) {
                generator.nextBytes(bytes)
                inBuffer.rewind()
                charsetDecoder.reset()
                charsetDecoder.decode(inBuffer, outBuffer, false)
            }
            outBuffer.flip()
            return outBuffer.toString()
        }

        fun getUUID(): String{
            return UUID.randomUUID().toString()
        }

        fun getOSVersion(): String {
            return Build.VERSION.SDK_INT.toString()
        }
    }
}