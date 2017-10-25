package blockchainForAccounts

import org.fusesource.leveldbjni.JniDBFactory
import org.iq80.leveldb.CompressionType
import org.iq80.leveldb.DB
import org.iq80.leveldb.DBIterator
import org.iq80.leveldb.Options
import java.io.File
import java.lang.Exception
import java.util.*

class DBWrapper(file: File) {
	private val db: DB

	init {
		val options = Options()
		options.createIfMissing(true)
		options.compressionType(CompressionType.NONE)
		db = JniDBFactory.factory.open(file, options)
	}

	fun close() = db.close()

	fun put(key: ByteArray, value: ByteArray): Boolean {
		try {
			db.put(key, value)
		} catch (e: Exception) {
			return false
		}
		return true
	}

	fun delete(bytes: ByteArray): Boolean {
		try {
			db.delete(bytes)
		} catch (e: Exception) {
			return false
		}
		return true
	}

	fun get(key: ByteArray): ByteArray = try { db.get(key) } catch (e: Exception) { byteArrayOf() }

	fun contains(key: ByteArray) = !get(key).isEmpty()

	fun containsValue(value: ByteArray): Boolean {
		val it: DBIterator = db.iterator()
		it.seekToFirst()

		while (it.hasNext()) {
			if (Arrays.equals(it.peekNext().value, value)) {
				it.close()
				return true
			}
			it.next()
		}
		it.close()
		return false
	}
}