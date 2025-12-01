package moaihead.firestore

import moaihead.data.PlainMoodRecord


/**
 * Converts [PlainMoodRecord] info a [Map] to be stored in [com.google.firebase.firestore.FirebaseFirestore]
 * using [FirestoreRepo].
 * @author Miroslav Hýbler <br>
 * created on 18.11.2025
 */
fun PlainMoodRecord.toFirestore(): Map<String, Any?> {
    return mapOf(
        "mood" to mood,
        "timestamp" to timestamp,
        "note" to note,
        "source" to source,
    )
}