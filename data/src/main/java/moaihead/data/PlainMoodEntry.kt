package moaihead.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


/**
 * @author Miroslav Hýbler <br>
 * created on 18.11.2025
 */
@Serializable
data class PlainMoodEntry constructor(
    val mood: Int,
    val timestamp: Long,
    val note: String?,
    val source: Int,
) {

    companion object {
        private val json = Json(builderAction = {
            ignoreUnknownKeys = true
        })
    }


    object Serializer {
        fun encode(record: PlainMoodEntry): ByteArray {
            return json.encodeToString(value = record).encodeToByteArray()
        }

        fun decode(bytes: ByteArray): PlainMoodEntry {
            return json.decodeFromString(string = bytes.decodeToString())
        }
    }
}