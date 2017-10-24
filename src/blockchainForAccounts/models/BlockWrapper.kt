package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.ByteUtils.toByteArray
import blockchainForAccounts.ByteUtils.toLong
import blockchainForAccounts.DBWrapper
import java.io.File

internal class BlockWrapper(block: Block, private val db: DBWrapper, private var count: Long) : Block(block) {
	private val entries = ArrayList<Entry>()

	fun add(entry: Entry): Boolean =
			entry.isAcceptable(db) &&
					!entries.contains(entry) &&
					entries.add(entry) &&
					entryKeys.add(entry.entryKeyHex())

	fun release(): Boolean {
		if (entries.isEmpty())
			return false

		db.put(KEY_BLOCKS_COUNTER, (++count).toByteArray())
		db.put(count.toByteArray(), toByteArray())
		entries.forEach { it.store(db) }
		entries.clear()

		return true
	}

	fun close() = db.close()

	companion object {
		private val BASE_DIR_NAME = "/accounts"
		private val INITIAL = "[_________initial________block_________]"
		private val KEY_BLOCKS_COUNTER = "blocks_counter".toByteArray()

		fun newInstance(parentPath: String): BlockWrapper {
			val db = DBWrapper(File(parentPath + BASE_DIR_NAME))
			val counter = db.get(KEY_BLOCKS_COUNTER)
			val previousBlock = if (counter.isEmpty()) null else db.get(counter)
			val previousHash = if (previousBlock == null) INITIAL else ByteUtils.hashHex(previousBlock)

			return BlockWrapper(
					Block(previousHash, System.currentTimeMillis(), ArrayList()),
					db,
					if (counter.isEmpty()) 0L else counter.toLong()
			)
		}
	}
}