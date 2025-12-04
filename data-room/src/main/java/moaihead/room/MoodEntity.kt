package moaihead.room

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import moaihead.data.model.EntrySource
import moaihead.data.model.Mood
import moaihead.data.model.MoodEntry
import moaihead.data.model.PlainMoodEntry
import java.time.Instant


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@Keep
@Entity(tableName = "mood")
public data class MoodEntity constructor(
    @ColumnInfo(name = "mood", typeAffinity = ColumnInfo.INTEGER)
    val mood: Int,
    @ColumnInfo(name = "timestamp", typeAffinity = ColumnInfo.INTEGER)
    @PrimaryKey(autoGenerate = false)
    val timestamp: Long,
    @ColumnInfo(name = "note", typeAffinity = ColumnInfo.TEXT, defaultValue = "null")
    val note: String?,
    @ColumnInfo(name = "source", typeAffinity = ColumnInfo.INTEGER)
    val source: Int,
    @ColumnInfo(name = "is_synchronized", typeAffinity = ColumnInfo.INTEGER)
    val isSynchronized: Boolean = false,
) {

    companion object {
        fun from(entry: PlainMoodEntry): MoodEntity {
            return MoodEntity(
                mood = entry.mood,
                timestamp = entry.timestamp,
                note = entry.note,
                source = entry.source,
            )
        }

        fun from(entry: MoodEntry): MoodEntity {
            return MoodEntity(
                mood = entry.mood.value,
                timestamp = entry.timestamp.toEpochMilli(),
                note = entry.note,
                source = entry.source.value,
            )
        }
    }


    fun toMoodEntry(): MoodEntry {
        return MoodEntry(
            mood = Mood.entries.first(predicate = { it.value == mood }),
            timestamp = Instant.ofEpochMilli(timestamp),
            note = note,
            source = EntrySource.entries.first(predicate = { it.value == source }),
        )
    }


    fun toPlainMoodEntry(): PlainMoodEntry {
        return PlainMoodEntry(
            mood = mood,
            timestamp = timestamp,
            note = note,
            source = source,
        )
    }

}