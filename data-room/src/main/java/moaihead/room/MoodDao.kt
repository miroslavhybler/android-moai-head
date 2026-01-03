package moaihead.room

import androidx.room.Dao
import androidx.room.Query


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@Dao
internal abstract class MoodDao : LocalDatabase.BaseDao<MoodEntity> {


    @Query(value ="SELECT * FROM mood")
    internal abstract suspend fun getAll(): List<MoodEntity>


    @Query(value ="SELECT * FROM mood WHERE is_synchronized = false")
    internal abstract suspend fun getAllNonSynchronized(): List<MoodEntity>
}