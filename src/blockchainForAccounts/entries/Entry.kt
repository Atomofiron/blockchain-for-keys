package blockchainForAccounts.entries

internal abstract class Entry {
	abstract val size: Int
	abstract val valid: Boolean

	abstract fun toByteArray(): ByteArray
	abstract override fun equals(other: Any?): Boolean
}