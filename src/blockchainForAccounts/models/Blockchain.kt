package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.DBWrapper
import blockchainForAccounts.GsonS
import com.google.gson.annotations.SerializedName
import java.io.File

internal class Blockchain constructor(parentPath: String) {
	private val db = DBWrapper(File(parentPath + BASE_DIR_NAME))
	private var block: Block
	private val entries = ArrayList<Entry>()

	init {
		var bytes = db.get(INITIAL.toByteArray())
		var hash: ByteArray? = null

		while (!bytes.isEmpty()) {
			hash = ByteUtils.hash(bytes)
			bytes = db.get(hash)
		}

		block = if (hash == null) Block() else Block(ByteUtils.byteArrayToHex(hash))
	}

	fun add(entry: Entry): Boolean =
			entry.isAcceptable(db) &&
					!entries.contains(entry) &&
					entries.add(entry) &&
					block.add(entry.entryKeyHex())

	fun release(): Boolean {
		if (entries.isEmpty())
			return false

		val bytes = block.toByteArray()
		db.put(block.getPrevHash(), bytes)
		entries.forEach { it.store(db) }
		entries.clear()

		block = Block(bytes)

		return true
	}

	fun close() = db.close()

	companion object {
		private val INITIAL = "[_________initial________block_________]"
		private val BASE_DIR_NAME = "/accounts"
	}

	private class Block(@SerializedName("h") private var prevHash: String = INITIAL) {
		@SerializedName("t")
		private val timestamp = System.currentTimeMillis()
		@SerializedName("s")
		private val entryKeys = ArrayList<String>()

		constructor(previousBlock: ByteArray) : this() {
			prevHash = ByteUtils.hashHex(previousBlock)
		}

		fun getPrevHash() = ByteUtils.hexToByteArray(prevHash) ?: prevHash.toByteArray()

		fun add(entryKeyHex: String) = entryKeys.add(entryKeyHex)

		fun toByteArray() = GsonS.instance.toJson(this).toByteArray()

		companion object {
			fun fromByteArray(bytes: ByteArray) =
					GsonS.instance.fromJson<Block>(String(bytes), Block::class.java)
		}
	}
}