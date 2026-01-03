@file:Suppress("RedundantVisibilityModifier")

package moaihead.room

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moaihead.data.BaseDataSourceRepository
import moaihead.data.model.MoodEntry
import moaihead.data.model.PlainMoodEntry
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@Singleton
public class LocalDatabaseRepo @Inject internal constructor(
    @ApplicationContext context: Context,
) : BaseDataSourceRepository() {

    private val localDatabase: LocalDatabase = LocalDatabase.create(context = context)

    private val mMoodData: MutableStateFlow<List<MoodEntry>> = MutableStateFlow(value = emptyList())
    public override val moodData: StateFlow<List<MoodEntry>> = mMoodData.asStateFlow()

    private val coroutineScope = CoroutineScope(
        context = Dispatchers.IO
            .plus(context = CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            })
            .plus(context = CoroutineName(name = "LocalDatabaseRepo"))
    )


    init {
        coroutineScope.launch {
            loadAllMoodData()
        }
    }


    private val moodDao: MoodDao
        get() = localDatabase.moodDao()

    override suspend fun getAllMoodEntries(): List<MoodEntry> {
        return moodDao.getAll().map(transform = MoodEntity::toMoodEntry)
    }


    public override suspend fun loadAllMoodData() {
        mMoodData.value = moodDao.getAll().map(transform = MoodEntity::toMoodEntry)
    }

    public override suspend fun insertOrUpdateMood(entry: MoodEntry) {
        return insertOrUpdateMood(entry = entry.toPlain())
    }

    public override suspend fun insertOrUpdateMood(entry: PlainMoodEntry) {
        moodDao.insert(item = MoodEntity.from(entry = entry))

    }

    public override suspend fun deleteMood(entry: MoodEntry) {
        moodDao.delete(item = MoodEntity.from(entry = entry))
    }

    public suspend fun insertOrUpdateMood(entry: MoodEntity) {
        moodDao.insert(item = entry)
    }

    public suspend fun loadAllNotSynced(): List<MoodEntity> {
        return moodDao.getAllNonSynchronized()
    }
}