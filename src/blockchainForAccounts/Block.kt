package blockchainForAccounts

import blockchainForAccounts.entries.Entry
import blockchainForAccounts.entries.KeyEntry
import blockchainForAccounts.ByteUtils.toByteArray

class Block private constructor(private val previousHash: String) {
	private val timestamp = System.currentTimeMillis()
	private val keyEntries = ArrayList<KeyEntry>()

	constructor() : this("=[_initial__block_]=")

	constructor(previousBlock: Block) : this(previousBlock.getHash())

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