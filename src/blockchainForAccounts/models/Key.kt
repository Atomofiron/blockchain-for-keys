package blockchainForAccounts.models

import blockchainForAccounts.ByteUtils
import blockchainForAccounts.GsonS
import com.google.gson.annotations.SerializedName
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

open internal class Key constructor(
		@SerializedName("n")
		protected val nick: String,
		@SerializedName("k")
		protected val publicKey: String
) {

	fun id() = ByteUtils.fromBase64(publicKey.toByteArray())

	fun toByteArray() = GsonS.instance.toJson(this).toByteArray()

	companion object {
		private val ALGORITHM = "RSA"

		fun fromByteArray(bytes: ByteArray): Key =
				GsonS.instance.fromJson<Key>(String(bytes), Key::class.java)

		fun encode(key: PublicKey) = String(Base64.getEncoder().encode(key.encoded))

		fun decode(publicKey: String) = KeyFactory.getInstance(ALGORITHM)
				.generatePublic(X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)))!!
	}
}