package blockchainForAccounts.entries

internal class BookingNickEntry(
		/** hash of the nick */
		val hashedNick: String,
		/** nick signature */
		val signedNick: String
) : Entry() {
	override val size = hashedNick.length + signedNick.length
	override val valid = false // todo some verification

	override fun toByteArray() = (hashedNick + signedNick).toByteArray()

	override fun equals(other: Any?) =
			other != null && other is BookingNickEntry && hashedNick == other.hashedNick
}