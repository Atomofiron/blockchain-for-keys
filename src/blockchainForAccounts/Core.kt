package blockchainForAccounts

import blockchainForAccounts.models.*
import java.security.PrivateKey
import java.security.PublicKey

class Core(dirPath: String) {
	private val block = BlockWrapper.newInstance(dirPath)

	fun bookNick(nick: String, key: PrivateKey) =
			block.add(BookingNickEntry(nick, key))

	fun addKey(nick: String, key: PublicKey) =
			block.add(KeyEntry(nick, key))

	fun releaseBlock() = block.release()

	fun close() = block.close()
}
