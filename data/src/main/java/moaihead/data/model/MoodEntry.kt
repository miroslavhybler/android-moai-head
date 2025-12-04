package moaihead.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import moaihead.data.InstantSerializer
import java.time.Instant


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@Serializable
@Keep
data class MoodEntry constructor(
    val mood: Mood,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant,
    val note: String?,
    val source: EntrySource,
) {

    fun toPlain(): PlainMoodEntry = PlainMoodEntry(
        mood = mood.value,
        timestamp = timestamp.toEpochMilli(),
        note = note,
        source = source.value,
    )
}