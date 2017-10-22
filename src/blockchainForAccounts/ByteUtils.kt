package blockchainForAccounts

import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern

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

	fun hexToByteArray(hex: String): ByteArray? {
		if (Pattern.compile("[^0-9a-fA-F]").matcher(hex).find())
			return null

		val hexChars = (if (hex.length % 2 == 1) "0" + hex else hex).toLowerCase().toCharArray()
		val bytes = ByteArray(hexChars.size / 2)

		for (i in hexChars.indices step 2)
			bytes[i / 2] = (hexArray.indexOf(hexChars[i]) * 16 + hexArray.indexOf(hexChars[i + 1])).toByte()

		return bytes
	}

	fun toBase64(bytes: ByteArray): ByteArray = Base64.getEncoder().encode(bytes)

	fun fromBase64(bytes: ByteArray): ByteArray = Base64.getDecoder().decode(bytes)

	fun hash(bytes: ByteArray): String
			= byteArrayToHex(MessageDigest.getInstance("SHA-1").digest(bytes))

	fun Long.toByteArray(): ByteArray = ByteBuffer.allocate(8).putLong(this).array()
}