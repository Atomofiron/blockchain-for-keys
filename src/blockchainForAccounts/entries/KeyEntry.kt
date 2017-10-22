package blockchainForAccounts.entries

import java.security.PublicKey

class KeyEntry(
		/** hash of the public key */
		val id: String,
		/** if this nick is not booked, this field must be empty, or this entry is unacceptable
		 *  if this nick is booked by another account, this entry is unacceptable */
		val nick: String,
		/** public key */
		val publicKey: PublicKey
) : Entry() {
	override val size = id.length + nick.length + publicKey.encoded.size
	override val valid = nick.isEmpty() // todo signature verification

	override fun toByteArray() = (id + nick).toByteArray() + publicKey.encoded

	override fun equals(other: Any?) =
			other != null && other is KeyEntry && id == other.id
}