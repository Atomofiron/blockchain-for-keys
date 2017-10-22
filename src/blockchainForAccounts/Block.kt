package blockchainForAccounts

import blockchainForAccounts.entries.Entry
import blockchainForAccounts.entries.KeyEntry
import blockchainForAccounts.ByteUtils.toByteArray

class Block(previousBlock: Block?) {
	private val previousHash = previousBlock?.getHash() ?: "=[_initial__block_]="
	private val timestamp = System.currentTimeMillis()
	private val keyEntries = ArrayList<KeyEntry>()

	constructor() : this(null)

	fun add(entry: Entry): Boolean {
		if (!entry.valid)
			return false

		if (entry is KeyEntry && !keyEntries.contains(entry))
			return keyEntries.add(entry) // always true

		return false
	}

	fun contains(entry: Entry): Boolean {
		if (entry is KeyEntry)
			return keyEntries.contains(entry)

		return false
	}

	fun isEmpty() = keyEntries.isEmpty()

	private fun getHash(): String {
		var bytes = byteArrayOf()

		bytes += previousHash.toByteArray()
		bytes += timestamp.toByteArray()
		keyEntries.forEach { bytes += it.toByteArray() }

		return ByteUtils.hash(bytes)
	}
}