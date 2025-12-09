package moaihead.data.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


/**
 * @author Miroslav Hýbler <br>
 * created on 08.12.2025
 */
suspend fun <T> Task<T>.awaitResult(): Result<T> {
    return suspendCancellableCoroutine(
        block = { cont ->
            addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cont.resume(value = Result.success(value = task.result))
                } else {
                    cont.resume(
                        value = Result.failure(
                            exception = task.exception
                                ?: Exception("Task failed")
                        )
                    )
                }
            }
        }
    )
}
