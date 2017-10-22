package blockchainForAccounts

import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.*

object ByteUtils {
	private val hexArray = "0123456789abcdef".toCharArray()

	fun byteArrayToHex(bytes: ByteArray): String {
		val hexChars = CharArray(bytes.size * 2)
		for (i in bytes.indices) {
			val x = bytes[i].toInt() and 0xff
			hexChars[i * 2] = hexArray[x.ushr(4)]
			hexChars[i * 2 + 1] = hexArray[x and 0x0f]
		}
		return String(hexChars)
	}

	fun toBase64(bytes: ByteArray): ByteArray = Base64.getEncoder().encode(bytes)

	fun fromBase64(bytes: ByteArray): ByteArray = Base64.getDecoder().decode(bytes)

	fun hash(bytes: ByteArray): String
			= String(toBase64(MessageDigest.getInstance("SHA-256").digest(bytes)))

	fun Long.toByteArray(): ByteArray = ByteBuffer.allocate(8).putLong(this).array()
}