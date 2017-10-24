package blockchainForAccounts

import blockchainForAccounts.models.*
import java.security.KeyPair
import java.security.PrivateKey

class Core(dirPath: String) {
	private val block = BlockWrapper.newInstance(dirPath)

	fun bookNick(nick: String, key: PrivateKey) =
			block.add(BookingNickEntry(nick, key))

	fun addKey(nick: String, keyPair: KeyPair) =
			if (nick.isEmpty())
				block.add(KeyEntry(nick, keyPair.public).signNick(keyPair.private))
			else
				block.add(KeyEntry(nick, keyPair.public))

	fun releaseBlock() = block.release()

	fun close() = block.close()
}
