package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.DBWrapper
import java.security.KeyPair

internal class KeyEntry(nick: String, keyPair: KeyPair) : Key(nick, Key.encode(keyPair.public)), Entry {
	private val id = id()
	private val signedNick = ByteUtils.sign(nick.toByteArray(), keyPair.private)

	private fun keyBytes() = ByteUtils.hexToByteArray(id)!!

	override fun keyHex() = id

	/* проверка: id не дублируется, ник пустой и подписан, или забронирован аккаунтом с этим ключём */
	override fun isAcceptable(db: DBWrapper): Boolean =
			db.get(keyBytes()).isEmpty() &&
					(nick.isEmpty() && ByteUtils.verify(nick.toByteArray(), decode(publicKey), signedNick) ||
							!db.get(ByteUtils.hash(nick.toByteArray())).isEmpty() &&
							ByteUtils.verify(nick.toByteArray(), Key.decode(publicKey), db.get(ByteUtils.hash(nick.toByteArray()))))

	override fun store(db: DBWrapper): Boolean {
		val idBytes = keyBytes()
		var put = db.put(idBytes, toByteArray())

		if (put && !nick.isEmpty()) {
			put = db.put(nick.toByteArray(), idBytes)

			if (!put)
				db.delete(idBytes)
		}

		return put
	}

	override fun equals(other: Any?) = other != null && other is KeyEntry && id == other.id
}