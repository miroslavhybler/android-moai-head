package moaihead.data

import kotlinx.serialization.Serializable
import java.time.Instant


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@Serializable
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