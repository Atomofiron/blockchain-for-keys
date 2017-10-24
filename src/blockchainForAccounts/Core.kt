package blockchainForAccounts

import blockchainForAccounts.models.*
import java.security.KeyPair
import java.security.PrivateKey

class Core(dirPath: String) {
	private val blockchain = Blockchain(dirPath)

	fun bookNick(nick: String, key: PrivateKey) =
			blockchain.add(BookingNickEntry(nick, key))

	fun addKey(nick: String, keyPair: KeyPair) =
			if (nick.isEmpty())
				blockchain.add(KeyEntry(nick, keyPair.public).signNick(keyPair.private))
			else
				blockchain.add(KeyEntry(nick, keyPair.public))

	fun releaseBlock() = blockchain.release()

	fun close() = blockchain.close()
}
