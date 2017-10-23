package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.DBWrapper
import java.security.PrivateKey

internal class BookingNickEntry(nick: String, key: PrivateKey) : Entry {
	/** hashKey of the nick */
	val hashedNick: String = ByteUtils.hashHex(nick.toByteArray())
	/** nick signature */
	val signedNick: String = ByteUtils.byteArrayToHex(ByteUtils.sign(nick.toByteArray(), key))

	private fun keyBytes() = ByteUtils.hexToByteArray(hashedNick)!!

	override fun keyHex() = hashedNick

	/* проверка: ник не был уже забронирован */
	override fun isAcceptable(db: DBWrapper) = db.get(keyBytes()).isEmpty()

	override fun store(db: DBWrapper) = db.put(keyBytes(), ByteUtils.hexToByteArray(signedNick)!!)

	override fun equals(other: Any?): Boolean =
			other != null && other is BookingNickEntry && hashedNick == other.hashedNick
}