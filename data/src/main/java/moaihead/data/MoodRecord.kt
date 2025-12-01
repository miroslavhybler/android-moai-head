package moaihead.data

import java.time.Instant


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
data class MoodRecord constructor(
    val mood: Mood,
    val timestamp: Instant,
    val note: String?,
    val source: EntrySource,
) {

    fun toPlain(): PlainMoodRecord = PlainMoodRecord(
        mood = mood.value,
        timestamp = timestamp.toEpochMilli(),
        note = note,
        source = source.value,
    )
}