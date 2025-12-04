package moaihead.room

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import moaihead.data.DataSourceRepository
import moaihead.data.MoodEntry
import moaihead.data.PlainMoodEntry
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@Singleton
class LocalDatabaseRepo @Inject constructor() : DataSourceRepository {


    private val mMoodData: MutableStateFlow<List<MoodEntry>> = MutableStateFlow(value = emptyList())
    override val moodData: StateFlow<List<MoodEntry>> =mMoodData.asStateFlow()

    override fun loadAllMoodData() {
        TODO()
    }

    override fun insertOrUpdateMood(entry: MoodEntry) {
        return insertOrUpdateMood(entry = entry.toPlain())
    }

    override fun insertOrUpdateMood(entry: PlainMoodEntry) {
        TODO("Not yet implemented")
    }


    override fun deleteMood(entry: MoodEntry) {
        TODO("Not yet implemented")
    }



}