package blockchainForAccounts

import blockchainForAccounts.models.*
import java.security.PrivateKey
import java.security.PublicKey

class Core(dirPath: String) {
	private val blockchain = Blockchain(dirPath)

	fun bookNick(nick: String, key: PrivateKey) =
			blockchain.add(BookingNickEntry(nick, key))

	fun addKey(key: PublicKey) = blockchain.add(KeyEntry(key.encoded))

	fun addNick(nick: String, id: ByteArray) = blockchain.add(NickEntry(nick, id))

	fun releaseBlock() = blockchain.release()

	fun close() = blockchain.close()
}
