package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.GsonS
import com.google.gson.annotations.SerializedName
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

open internal class Key private constructor(
		@SerializedName("n")
		protected val nick: String,
		@SerializedName("k")
		private val publicKey: String // Base64
) {

	constructor(nick: String, key: PublicKey) : this(nick, ByteUtils.toBase64String(key.encoded))

	fun id() = ByteUtils.hash(ByteUtils.fromBase64String(publicKey))

	fun generate() = KeyFactory.getInstance(ALGORITHM)
			.generatePublic(X509EncodedKeySpec(ByteUtils.fromBase64String(publicKey)))!!

	fun toByteArray() = GsonS.instance.toJson(this).toByteArray()

	companion object {
		private val ALGORITHM = "RSA"

		fun fromByteArray(bytes: ByteArray) = GsonS.instance.fromJson<Key>(String(bytes), Key::class.java)
	}
}