package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.DBWrapper
import java.security.PrivateKey
import java.util.*

internal class BookingNickEntry(nick: String, key: PrivateKey) : Entry {
	/** twice hashed nick */
	private val twiceHashedNick = ByteUtils.hash(ByteUtils.hash(nick.toByteArray()))
	/** hashed nick signature */
	private val signedHashedNick = ByteUtils.sign(ByteUtils.hash(nick.toByteArray()), key)

	override fun entryKeyHex() = ByteUtils.byteArrayToHex(twiceHashedNick)

	/* проверка: ник не был уже забронирован */
	override fun isAcceptable(db: DBWrapper) = db.get(twiceHashedNick).isEmpty()

	override fun store(db: DBWrapper) = db.put(twiceHashedNick, signedHashedNick)

	override fun equals(other: Any?): Boolean =
			other is BookingNickEntry && Arrays.equals(twiceHashedNick, other.twiceHashedNick)
}