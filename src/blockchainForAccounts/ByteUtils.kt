package blockchainForAccounts

import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
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

	fun toBase64String(bytes: ByteArray): String = Base64.getEncoder().encodeToString(bytes)

	fun fromBase64String(data: String): ByteArray = Base64.getDecoder().decode(data)

	fun hash(bytes: ByteArray): ByteArray = MessageDigest.getInstance("SHA-1").digest(bytes)

	fun hashHex(bytes: ByteArray): String = byteArrayToHex(hash(bytes))

	fun Long.toByteArray(): ByteArray = ByteBuffer.allocate(8).putLong(this).array()

	fun ByteArray.toLong(): Long = ByteBuffer.wrap(this).long

	fun sign(bytes: ByteArray, key: PrivateKey): ByteArray {
		val rsa = Signature.getInstance("SHA1withRSA")
		rsa.initSign(key)
		rsa.update(bytes)
		return rsa.sign()
	}

	fun verify(bytes: ByteArray, key: PublicKey, sign: ByteArray): Boolean {
		val rsa = Signature.getInstance("SHA1withRSA")
		rsa.initVerify(key)
		rsa.update(bytes)
		return rsa.verify(sign)
	}
}