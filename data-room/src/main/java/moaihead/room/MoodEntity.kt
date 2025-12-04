package moaihead.room

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import moaihead.data.PlainMoodEntry


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@Keep
@Entity(tableName = "mood")
internal data class MoodEntity constructor(
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

        fun from(entry: MoodEntity): MoodEntity {
            return MoodEntity(
                mood = entry.mood,
                timestamp = entry.timestamp,
                note = entry.note,
                source = entry.source,
            )
        }
    }

}