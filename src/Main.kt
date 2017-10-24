import blockchainForAccounts.ByteUtils
import blockchainForAccounts.Core
import java.io.File
import java.security.*
import java.util.*

object Main {
	private val scan = Scanner(System.`in`)
	private lateinit var core: Core
	private lateinit var keyPair: KeyPair

	@JvmStatic
	fun main(args: Array<String>) {
		while (true) {
			when (scan.next()) {
				"init" -> init()
				"genKeys" -> genKeys()
				"bookNick" -> bookNick()
				"addKeyWithNick" -> addKey()
				"release" -> release()
				"q" -> {
					core.close()
					return
				}
				else -> I.println("wat")
			}
		}
	}

	private fun readNick(): String {
		I.println("Input nick:")
		val nick = scan.next(".*")

		if (nick.length > 1 && (nick.startsWith("\"") && nick.endsWith("\"") || nick.startsWith("\'") && nick.endsWith("\'")))
			return nick.replace(Regex("^.|.$"), "")

		return nick
	}

	private fun init() {
		I.println("/home/atomofiron/Android/\nInput dir path:")
		val dir = File(scan.next())

		if (dir.exists() && dir.canRead()) {
			core = Core(dir.absolutePath)
			I.println("done")
		} else
			I.println("fck")
	}

	private fun bookNick() {
		val nick = readNick()

		if (core.bookNick(nick, keyPair.private))
			I.println("yeah")
		else
			I.println("fck")
	}

	private fun genKeys() {
		val generator = KeyPairGenerator.getInstance("RSA")
		val random = SecureRandom.getInstance("SHA1PRNG", "SUN")
		generator.initialize(2048, random)
		keyPair = generator.generateKeyPair()

		I.println("id:	        " + ByteUtils.hashHex(keyPair.public.encoded))
		I.println("public key:  " + String(ByteUtils.toBase64(keyPair.public.encoded)))
		I.println("private key: " + String(ByteUtils.toBase64(keyPair.private.encoded)))
	}

	private fun addKey() {
		if (core.addKey(readNick(), keyPair))
			I.println("yeah")
		else
			I.println("fck")
	}

	private fun release() {
		if (core.releaseBlock())
			I.println("yeah")
		else
			I.println("fck")
	}
}