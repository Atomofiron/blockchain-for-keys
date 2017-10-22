import blockchainForAccounts.ByteUtils
import blockchainForAccounts.Core
import java.security.*
import java.util.*

object Main {
	val scan = Scanner(System.`in`)
	val core = Core()
	lateinit var keyPair: KeyPair

	@JvmStatic
	fun main(args: Array<String>) {
		while (true) {
			when (scan.next()) {
				"key" -> genKey()
				"release" -> release()
				"q" -> return
				else -> I.println("wat")
			}
		}
	}

	private fun genKey() {
		val generator = KeyPairGenerator.getInstance("RSA")
		val random = SecureRandom.getInstance("SHA1PRNG", "SUN")
		generator.initialize(2048, random)
		val pair = generator.generateKeyPair()

		keyPair = pair

		if (core.addKey(pair.public)) {
			I.println("yeah")
			I.println("id:		  " + ByteUtils.hash(pair.public.encoded))
			I.println("public key:  " + ByteUtils.toBase64(pair.public.encoded))
			I.println("private key: " + ByteUtils.toBase64(pair.private.encoded))
		} else
			I.println("fck")
	}

	private fun release() {
		if (core.releaseBlock())
			I.println("yeah")
		else
			I.println("fck")
	}
}