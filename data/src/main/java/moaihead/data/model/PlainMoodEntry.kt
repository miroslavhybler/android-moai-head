package moaihead.data.model

import androidx.annotation.Keep
import com.mockup.annotations.Mockup
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.Instant


/**
 * @author Miroslav Hýbler <br>
 * created on 18.11.2025
 */
@Keep
@Serializable
@Mockup
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


    fun toMoodEntry(): MoodEntry {
        return MoodEntry(
            mood = Mood.entries.first(predicate = { it.value == mood }),
            timestamp = Instant.ofEpochMilli(timestamp),
            note = note,
            source = EntrySource.entries.first(predicate = { it.value == source }),
        )
    }


    object Serializer {
        fun encode(entry: PlainMoodEntry): ByteArray {
            return json.encodeToString(value = entry).encodeToByteArray()
        }

        fun decode(bytes: ByteArray): PlainMoodEntry {
            return json.decodeFromString(string = bytes.decodeToString())
        }
    }
}