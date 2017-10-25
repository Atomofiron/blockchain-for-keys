package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.DBWrapper
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.*

class NickEntry(nick: String, private val id: ByteArray) : Entry {
	private val hashedNick = ByteUtils.hash(nick.toByteArray())

	override fun entryKeyHex() = ByteUtils.byteArrayToHex(hashedNick)

	override fun isAcceptable(db: DBWrapper): Boolean {
		val signature = db.get(ByteUtils.hash(hashedNick))
		val publicKey = db.get(id)

		// ник был забронирован, ключ был зарегистрирован
		if (signature.isEmpty() || publicKey.isEmpty())
			return false

		// верификация подписи забронированного ника с использованием зарегистрированного ключа
		return ByteUtils.verify(hashedNick, generate(publicKey), signature)
	}

	override fun store(db: DBWrapper) = db.put(hashedNick, id)

	override fun equals(other: Any?) = other is NickEntry && Arrays.equals(hashedNick, other.hashedNick)

	companion object {
		private val ALGORITHM = "RSA"

		fun generate(publicKey: ByteArray) = KeyFactory.getInstance(ALGORITHM)
				.generatePublic(X509EncodedKeySpec(publicKey))!!
	}
}
