package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.DBWrapper
import java.security.PrivateKey
import java.security.PublicKey
import java.util.*

internal class KeyEntry constructor(nick: String, key: PublicKey) : Key(nick, key), Entry {
	private val id = id()
	private var signedNick: String = "" // Base64

	fun signNick(key: PrivateKey): KeyEntry {
		signedNick = ByteUtils.toBase64String(ByteUtils.sign(nick.toByteArray(), key))
		return this
	}

	override fun entryKeyHex() = ByteUtils.byteArrayToHex(id)

	override fun isAcceptable(db: DBWrapper): Boolean {
		// id не должен дублироваться
		if (!db.get(id).isEmpty())
			return false

		// ник пустой и подписан
		if (nick.isEmpty() && ByteUtils.verify(nick.toByteArray(), generate(), ByteUtils.fromBase64String(signedNick)))
			return true

		// забронирован аккаунтом с этим ключём
		val signature = db.get(ByteUtils.hash(nick.toByteArray()))
		return !signature.isEmpty() && ByteUtils.verify(nick.toByteArray(), generate(), signature)
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