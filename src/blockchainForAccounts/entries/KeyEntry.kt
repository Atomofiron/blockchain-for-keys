package blockchainForAccounts.entries

import java.security.PublicKey

class KeyEntry(
		/** hash of the public key */
		val id: String,
		/** if nick has not valid signature, this field must be empty, or this entry is invalid */
		val nick: String,
		/** publick key */
		val publicKey: PublicKey,
		/** nick signature */
		val signedNick: String
) : Entry() {
	override val size = id.length + nick.length + publicKey.encoded.size
	override val valid = nick.isEmpty() // todo signature verification

	override fun toByteArray() = (id + nick).toByteArray() + publicKey.encoded

	override fun equals(other: Any?) =
			other != null && other is KeyEntry && id == other.id
}