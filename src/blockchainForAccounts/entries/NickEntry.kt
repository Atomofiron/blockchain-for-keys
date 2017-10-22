package blockchainForAccounts.entries

class NickEntry(
		/** hash of the public key */
		val id: String,
		/** if nick has not valid signature, this entry is unacceptable */
		val nick: String,
		/** nick signature */
		val signedNick: String
) : Entry() {
	override val size = id.length + nick.length
	override val valid = false // todo signature verification

	override fun toByteArray() = (id + nick).toByteArray()

	override fun equals(other: Any?) =
			other != null && other is NickEntry && nick == other.nick
}