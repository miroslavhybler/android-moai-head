@file:Suppress(
    "RedundantVisibilityModifier",
    "RedundantModalityModifier",
    "RedundantUnitReturnType"
)

package moaihead.data

import kotlinx.coroutines.flow.StateFlow
import moaihead.data.model.MoodEntry
import moaihead.data.model.PlainMoodEntry


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
public interface DataSourceRepository {

    abstract val moodData: StateFlow<List<MoodEntry>>


    abstract suspend fun loadAllMoodData(): Unit


    abstract suspend fun insertOrUpdateMood(entry: MoodEntry): Unit


    abstract suspend fun insertOrUpdateMood(entry: PlainMoodEntry): Unit


    abstract suspend fun deleteMood(entry: MoodEntry): Unit

    abstract suspend fun getTotalAverageMood(): Float

}