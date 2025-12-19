@file:Suppress(
    "RedundantVisibilityModifier",
    "RedundantModalityModifier",
    "RedundantUnitReturnType"
)

package moaihead.data

import kotlinx.coroutines.flow.StateFlow
import moaihead.data.model.Mood
import moaihead.data.model.MoodEntry
import moaihead.data.model.PlainMoodEntry
import kotlin.collections.component1
import kotlin.collections.component2


/**
 * Base repository for handling mood data operations.
 * This class provides a common interface for accessing and manipulating mood entries,
 * regardless of the underlying data source (e.g., local database, remote server).
 *
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
public abstract class BaseDataSourceRepository public constructor() {


    /**
     * A flow of all mood entries, providing real-time updates.
     */
    abstract val moodData: StateFlow<List<MoodEntry>>


    /**
     * Loads all mood data from the data source.
     */
    abstract suspend fun loadAllMoodData(): Unit


    /**
     * Inserts a mood entry or updates it if it already exists.
     * @param entry The [MoodEntry] to be inserted or updated.
     */
    abstract suspend fun insertOrUpdateMood(entry: MoodEntry): Unit


    /**
     * Inserts a mood entry from a [PlainMoodEntry] or updates it if it already exists.
     * @param entry The [PlainMoodEntry] to be inserted or updated.
     */
    abstract suspend fun insertOrUpdateMood(entry: PlainMoodEntry): Unit


    /**
     * Deletes a mood entry.
     * @param entry The [MoodEntry] to be deleted.
     */
    abstract suspend fun deleteMood(entry: MoodEntry): Unit


    /**
     * Calculates the total average mood.
     * @return The average mood as a [Float].
     */
    abstract suspend fun getTotalAverageMood(): Float


    /**
     * Groups mood notes by mood type.
     * This function analyzes the `moodData` and groups notes associated with each mood.
     *
     * @return A [HashMap] where keys are [Mood]s and values are lists of pairs,
     * with each pair containing a note ([String]) and its frequency ([Int]).
     */
    suspend fun groupModeNotes(): HashMap<Mood, List<Pair<String, Int>>> {
        if (moodData.value.isEmpty()) {
            loadAllMoodData()
        }


        val output = HashMap<Mood, List<Pair<String, Int>>>()

        Mood.entries.forEach { mood ->
            val filtered = moodData.value
                .filter(predicate = { it.mood == mood })
                .filter(predicate = { it.note != null })

            val grouped = filtered.groupBy(keySelector = { it.note!! })
            output[mood] = grouped
                .map(transform = { (note, entries) -> note to entries.size })
        }

        return output
    }
}