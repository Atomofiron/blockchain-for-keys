package blockchainForAccounts.models

import blockchainForAccounts.DBWrapper

interface Entry {
	fun entryKeyHex(): String
	fun entryHashHex(): String
	fun isAcceptable(db: DBWrapper): Boolean
	fun store(db: DBWrapper): Boolean
	override fun equals(other: Any?): Boolean
}