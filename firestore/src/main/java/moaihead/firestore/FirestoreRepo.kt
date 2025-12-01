package moaihead.firestore

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import moaihead.data.EntrySource
import moaihead.data.Mood
import moaihead.data.MoodRecord
import moaihead.data.PlainMoodRecord
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Miroslav Hýbler <br>
 * created on 18.11.2025
 */
@Singleton
class FirestoreRepo @Inject constructor() {


    private val firestore: FirebaseFirestore = Firebase.firestore

    private val mIsAuthorized: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    val isAuthorized: StateFlow<Boolean> = mIsAuthorized.asStateFlow()


    private val mAllData: MutableStateFlow<List<MoodRecord>> = MutableStateFlow(value = emptyList())
    val allData: StateFlow<List<MoodRecord>> = mAllData.asStateFlow()


    init {
   signIn()
    }

    fun signIn(){
        Firebase.auth
            .signInAnonymously()
            .addOnSuccessListener {
                mIsAuthorized.value = true
                test()
                readAllData()
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreRepo", "Error signing in", e)
                mIsAuthorized.value = false
            }
    }


    fun insertMood(moodRecord: MoodRecord) {
        if (!isAuthorized.value) {
            return
        }

        insertMood(plainMoodRecord = moodRecord.toPlain())
    }


    fun insertMood(plainMoodRecord: PlainMoodRecord) {
        if (!isAuthorized.value) {
            return
        }

        firestore.collection("mood")
            .document("${plainMoodRecord.timestamp}")
            .set(plainMoodRecord.toFirestore())
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->
                Log.w("FirestoreRepo", "Error adding document", e)
            }
    }


    fun test() {
        if (!isAuthorized.value) {
            return
        }

        firestore.collection("mood")
            .get()
            .addOnSuccessListener {
                Log.d("FirestoreRepo", "Success reading collection, size: ${it.documents.size}")

                if (it.documents.isEmpty() || it.documents.size == 1) {
                    insertMood(
                        plainMoodRecord = PlainMoodRecord(
                            mood = 10,
                            timestamp = System.currentTimeMillis(),
                            note = null,
                            source = 1,
                        )
                    )
                }
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreRepo", "Error reading collection", e)
            }
    }


    fun readAllData() {
        firestore.collection("mood")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { collection ->
                val list = collection.documents.mapNotNull { doc ->
                    val mood = doc.getLong("mood")?.toInt() ?: return@mapNotNull null
                    val timestamp = doc.getLong("timestamp") ?: return@mapNotNull null
                    val note = doc.getString("note")
                    val source = doc.getLong("source")?.toInt() ?: return@mapNotNull null

                    MoodRecord(
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


}