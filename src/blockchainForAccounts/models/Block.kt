package blockchainForAccounts.models

import blockchainForAccounts.GsonS
import com.google.gson.annotations.SerializedName

open internal class Block(
		@SerializedName("h")
		private val previousHash: String,
		@SerializedName("t")
		private val timestamp: Long,
		@SerializedName("s")
		protected val entryKeys: ArrayList<String>
) {

	constructor(block: Block) : this(block.previousHash, block.timestamp, block.entryKeys)

	fun toByteArray() = GsonS.instance.toJson(this).toByteArray()

	companion object {
		fun fromByteArray(bytes: ByteArray) =
				GsonS.instance.fromJson<Block>(String(bytes), Block::class.java)
	}
}