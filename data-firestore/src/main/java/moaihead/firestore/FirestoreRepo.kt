package moaihead.firestore

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moaihead.data.BaseDataSourceRepository
import moaihead.data.model.EntrySource
import moaihead.data.model.Mood
import moaihead.data.model.MoodEntry
import moaihead.data.model.PlainMoodEntry
import moaihead.data.utils.awaitResult
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Miroslav Hýbler <br>
 * created on 18.11.2025
 */
@Singleton
class FirestoreRepo @Inject constructor() : BaseDataSourceRepository() {

    companion object {
        const val MOOD_COLLECTION: String = "mood"
    }


    private val firestore: FirebaseFirestore = Firebase.firestore

    private val mIsAuthorized: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    val isAuthorized: StateFlow<Boolean> = mIsAuthorized.asStateFlow()


    private var isAuthorizing: Boolean = false

    private val mAllData: MutableStateFlow<List<MoodEntry>> = MutableStateFlow(value = emptyList())
    override val moodData: StateFlow<List<MoodEntry>> = mAllData.asStateFlow()


    private val coroutineScope = CoroutineScope(
        context = Dispatchers.IO
            .plus(context = CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            })
            .plus(context = CoroutineName(name = "FirestoreRepo"))
    )


    init {
        coroutineScope.launch {
            signIn()
        }
    }

    override suspend fun getAllMoodEntries(): List<MoodEntry> {
        return moodData.value
    }


    override suspend fun loadAllMoodData() {
        firestore.collection(MOOD_COLLECTION)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { collection ->
                val list = collection.documents.mapNotNull { doc ->
                    val mood = doc.getLong("mood")?.toInt() ?: return@mapNotNull null
                    val timestamp = doc.getLong("timestamp") ?: return@mapNotNull null
                    val note = doc.getString("note")
                    val source = doc.getLong("source")?.toInt() ?: return@mapNotNull null

                    MoodEntry(
                        mood = Mood.entries
                            .find(predicate = { it.value == mood })
                            ?: return@mapNotNull null,
                        timestamp = Instant.ofEpochMilli(timestamp),
                        note = note,
                        source = EntrySource.entries
                            .find(predicate = { it.value == source })
                            ?: return@mapNotNull null,
                    )
                }
                mAllData.value = list
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    suspend fun signIn() {
        if (isAuthorized.value || isAuthorizing) {
            //Already authorized
            return
        }

        isAuthorizing = true
        Firebase.auth
            .signInAnonymously()
            .addOnSuccessListener {
                mIsAuthorized.value = true
                coroutineScope.launch {
                    loadAllMoodData()
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                mIsAuthorized.value = false
            }.addOnCompleteListener {
                isAuthorizing = false
            }
    }


    suspend fun syncMoodEntries(
        entries: List<MoodEntry>,
    ): Result<Unit> {
        if (!isAuthorized.value || entries.isEmpty()) {
            return Result.success(value = Unit)
        }

        val batch = firestore.batch()
        for (entry in entries) {
            val docRef = firestore.collection(MOOD_COLLECTION)
                .document("${entry.timestamp.toEpochMilli()}")
            batch.set(docRef, entry.toPlain())
        }

        var fault: Exception? = null
        batch.commit()
            .addOnSuccessListener {
                coroutineScope.launch {
                    loadAllMoodData()
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                fault = exception
            }
            .awaitResult()

        return if (fault != null) {
            Result.failure(exception = fault)
        } else {
            Result.success(value = Unit)
        }
    }


    override suspend fun insertOrUpdateMood(entry: MoodEntry) {
        if (!isAuthorized.value) {
            return
        }

        insertOrUpdateMood(entry = entry.toPlain())
    }


    override suspend fun insertOrUpdateMood(entry: PlainMoodEntry) {
        if (!isAuthorized.value) {
            return
        }

        firestore.collection("mood")
            .document("${entry.timestamp}")
            .set(entry.toFirestore())
            .addOnSuccessListener {
                coroutineScope.launch {
                    loadAllMoodData()
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }


    override suspend fun deleteMood(entry: MoodEntry) {
        if (!isAuthorized.value) {
            return
        }

        deleteMood(entry = entry.toPlain())
    }


    fun deleteMood(entry: PlainMoodEntry) {
        if (!isAuthorized.value) {
            return
        }

        firestore.collection("mood")
            .document("${entry.timestamp}")
            .delete()
            .addOnSuccessListener {
                mAllData.value = mAllData.value.filter(
                    predicate = { it.timestamp.toEpochMilli() != entry.timestamp })
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}