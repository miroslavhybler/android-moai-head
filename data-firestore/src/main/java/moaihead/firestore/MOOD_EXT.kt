package moaihead.firestore

import moaihead.data.model.PlainMoodEntry


/**
 * Converts [PlainMoodEntry] info a [Map] to be stored in [com.google.firebase.firestore.FirebaseFirestore]
 * using [FirestoreRepo].
 * @author Miroslav Hýbler <br>
 * created on 18.11.2025
 */
fun PlainMoodEntry.toFirestore(): Map<String, Any?> {
    return mapOf(
        "mood" to mood,
        "timestamp" to timestamp,
        "note" to note,
        "source" to source,
    )
}