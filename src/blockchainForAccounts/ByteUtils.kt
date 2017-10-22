package blockchainForAccounts

import java.nio.ByteBuffer
import java.security.MessageDigest

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

	fun hash(bytes: ByteArray): String
			= byteArrayToHex(MessageDigest.getInstance("SHA-256").digest(bytes))

	fun Long.toByteArray() =
			ByteBuffer.allocate(8).putLong(this).array()!!
}