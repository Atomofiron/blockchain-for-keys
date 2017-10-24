package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.DBWrapper
import java.security.PrivateKey
import java.util.*

internal class BookingNickEntry(nick: String, key: PrivateKey) : Entry {
	/** hash of the nick */
	private val hashedNick = ByteUtils.hash(nick.toByteArray())
	/** nick signature */
	private val signedNick = ByteUtils.sign(nick.toByteArray(), key)

	override fun entryKeyHex() = ByteUtils.byteArrayToHex(hashedNick)

	/* проверка: ник не был уже забронирован */
	override fun isAcceptable(db: DBWrapper) = db.get(hashedNick).isEmpty()

	override fun store(db: DBWrapper) = db.put(hashedNick, signedNick)

	override fun equals(other: Any?): Boolean =
			other != null && other is BookingNickEntry && Arrays.equals(hashedNick, other.hashedNick)
}