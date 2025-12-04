@file:Suppress(
    "RedundantVisibilityModifier",
    "RedundantConstructorKeyword",
    "RedundantUnitReturnType"
)

package moaihead.room

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


/**
 * @author Miroslav Hýbler <br>
 * created on 20.08.2024
 */
@Database(
    entities = [
        MoodEntity::class,
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = [],
)
@TypeConverters(
    value = [

    ]

)
internal abstract class LocalDatabase public constructor() : RoomDatabase() {

    companion object {

        internal fun create(context: Context): LocalDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = LocalDatabase::class.java,
                name = "PushUps-database"
            ).fallbackToDestructiveMigration(dropAllTables = false)
                .build()
        }
    }


    internal abstract fun moodDao(): MoodDao


    /**
     * Base DAO interface.
     */
    internal interface BaseDao<T> {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(item: T): Long

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(items: List<T>): Unit

        @Update(onConflict = OnConflictStrategy.REPLACE)
        suspend fun update(item: T): Unit

        @Update(onConflict = OnConflictStrategy.REPLACE)
        suspend fun update(items: List<T>): Unit

        @Delete
        suspend fun delete(item: T): Unit

        @Delete
        suspend fun delete(items: List<T>): Unit
    }
}