package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.DBWrapper
import java.util.*

internal class KeyEntry(private val publicKey: ByteArray) :  Entry {
	private val id = ByteUtils.hash(publicKey)

	override fun entryKeyHex() = ByteUtils.byteArrayToHex(id)

	override fun entryHashHex() = entryKeyHex()

	override fun isAcceptable(db: DBWrapper) = db.get(id).isEmpty()

	override fun store(db: DBWrapper) = db.put(id, publicKey)

	override fun equals(other: Any?) = other is KeyEntry && Arrays.equals(id, other.id)
}