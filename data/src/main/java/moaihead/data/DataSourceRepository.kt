@file:Suppress(
    "RedundantVisibilityModifier",
    "RedundantModalityModifier",
    "RedundantUnitReturnType"
)

package moaihead.data

import kotlinx.coroutines.flow.StateFlow


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
public interface DataSourceRepository {

    abstract val moodData: StateFlow<List<MoodEntry>>


    abstract fun loadAllMoodData(): Unit


    abstract fun insertOrUpdateMood(entry: MoodEntry): Unit


    abstract fun insertOrUpdateMood(entry: PlainMoodEntry): Unit


    abstract fun deleteMood(entry: MoodEntry): Unit


}