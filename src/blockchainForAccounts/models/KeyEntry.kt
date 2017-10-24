package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.DBWrapper
import java.security.KeyPair
import java.util.*

internal class KeyEntry(nick: String, keyPair: KeyPair) : Key(nick, Key.encode(keyPair.public)), Entry {
	private val id = id()
	private val signedNick = ByteUtils.sign(nick.toByteArray(), keyPair.private)

	override fun entryKeyHex() = ByteUtils.byteArrayToHex(id)

	override fun isAcceptable(db: DBWrapper): Boolean {
		// id не должен дублироваться
		if (!db.get(id).isEmpty())
			return false

		// ник пустой и подписан
		if (nick.isEmpty() && ByteUtils.verify(nick.toByteArray(), decode(publicKey), signedNick))
			return true

		// забронирован аккаунтом с этим ключём
		val signature = db.get(ByteUtils.hash(nick.toByteArray()))
		return !signature.isEmpty() && ByteUtils.verify(nick.toByteArray(), Key.decode(publicKey), signature)
	}

	override fun store(db: DBWrapper): Boolean {
		var put = db.put(id, toByteArray())

		if (put && !nick.isEmpty()) {
			put = db.put(nick.toByteArray(), id)

			if (!put)
				db.delete(id)
		}

		return put
	}

	override fun equals(other: Any?) = other != null && other is KeyEntry && Arrays.equals(id, other.id)
}