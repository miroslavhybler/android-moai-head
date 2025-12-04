package moaihead.firestore

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import moaihead.data.DataSourceRepository
import moaihead.data.EntrySource
import moaihead.data.Mood
import moaihead.data.MoodEntry
import moaihead.data.PlainMoodEntry
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Miroslav Hýbler <br>
 * created on 18.11.2025
 */
@Singleton
class FirestoreRepo @Inject constructor() : DataSourceRepository {


    private val firestore: FirebaseFirestore = Firebase.firestore

    private val mIsAuthorized: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    val isAuthorized: StateFlow<Boolean> = mIsAuthorized.asStateFlow()


    private var isAuthorizing: Boolean = false

    private val mAllData: MutableStateFlow<List<MoodEntry>> = MutableStateFlow(value = emptyList())
   override val moodData: StateFlow<List<MoodEntry>> = mAllData.asStateFlow()


    init {
        signIn()
    }


    override fun loadAllMoodData() {
        firestore.collection("mood")
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


    fun signIn() {
        if (isAuthorized.value || isAuthorizing) {
            //Already authorized
            return
        }

        isAuthorizing = true
        Firebase.auth
            .signInAnonymously()
            .addOnSuccessListener {
                mIsAuthorized.value = true
                loadAllMoodData()
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                mIsAuthorized.value = false
            }.addOnCompleteListener {
                isAuthorizing = false
            }
    }


    override fun insertOrUpdateMood(entry: MoodEntry) {
        if (!isAuthorized.value) {
            return
        }

        insertOrUpdateMood(entry = entry.toPlain())
    }


    override fun insertOrUpdateMood(entry: PlainMoodEntry) {
        if (!isAuthorized.value) {
            return
        }

        firestore.collection("mood")
            .document("${entry.timestamp}")
            .set(entry.toFirestore())
            .addOnSuccessListener {
                loadAllMoodData()
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }


    override fun deleteMood(entry: MoodEntry) {
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