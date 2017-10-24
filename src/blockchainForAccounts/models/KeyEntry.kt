package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.DBWrapper
import java.security.KeyPair
import java.util.*

internal class KeyEntry(nick: String, keyPair: KeyPair) : Key(nick, Key.encode(keyPair.public)), Entry {
	private val id = id()
	private val signedNick = ByteUtils.sign(nick.toByteArray(), keyPair.private)

	override fun entryKeyHex() = ByteUtils.byteArrayToHex(id)

	/* проверка: id не дублируется, ник пустой и подписан, или забронирован аккаунтом с этим ключём */
	override fun isAcceptable(db: DBWrapper): Boolean =
			db.get(id).isEmpty() &&
					(nick.isEmpty() && ByteUtils.verify(nick.toByteArray(), decode(publicKey), signedNick) ||
							!db.get(ByteUtils.hash(nick.toByteArray())).isEmpty() &&
							ByteUtils.verify(nick.toByteArray(), Key.decode(publicKey), db.get(ByteUtils.hash(nick.toByteArray()))))

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