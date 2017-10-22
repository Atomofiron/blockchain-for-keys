package blockchainForAccounts

import blockchainForAccounts.entries.Entry
import blockchainForAccounts.entries.KeyEntry
import java.security.PublicKey
import java.util.*

class Core {
	private val blocks = ArrayList<Block>()
	private var currentBlock: Block = Block()

	fun addKey(key: PublicKey) =
		add(KeyEntry(ByteUtils.hash(key.encoded), "", key))

	private fun add(entry: Entry): Boolean {
		if (blocks.find { it.contains(entry) } != null)
			return false

		return currentBlock.add(entry)
	}

	fun releaseBlock(): Boolean {
		if (currentBlock.isEmpty())
			return false

		val block = currentBlock
		blocks.add(block)
		currentBlock = Block(block)

		return true
	}
}
