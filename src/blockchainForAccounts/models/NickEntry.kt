package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.DBWrapper
import java.util.*

class NickEntry(nick: String, private val id: ByteArray) : Entry {
	private val hashedNick = ByteUtils.hash(nick.toByteArray())

	override fun entryKeyHex() = ByteUtils.byteArrayToHex(hashedNick)

	override fun entryHashHex() = ByteUtils.byteArrayToHex(id)

	override fun isAcceptable(db: DBWrapper): Boolean {
		// ник не был зарегистрирован ранее, и на данный id никакой ник не зарегистрирован
		if (db.containsKey(hashedNick) || db.containsValue(id))
			return false

		val signature = db.get(ByteUtils.hash(hashedNick))
		val publicKey = db.get(id)

		// ник был забронирован, ключ был зарегистрирован
		if (signature.isEmpty() || publicKey.isEmpty())
			return false

		// верификация подписи забронированного ника с использованием зарегистрированного ключа
		return ByteUtils.verify(hashedNick, ByteUtils.generatePublic(publicKey), signature)
	}

	override fun store(db: DBWrapper) = db.put(hashedNick, id)

	override fun equals(other: Any?) = other is NickEntry &&
			(Arrays.equals(hashedNick, other.hashedNick) || Arrays.equals(id, other.id))
}
