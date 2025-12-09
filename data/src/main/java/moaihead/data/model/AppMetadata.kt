package moaihead.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * @author Miroslav Hýbler <br>
 * created on 08.12.2025
 */
@Serializable
@Keep
data class AppMetadata constructor(
    val totalAverageMoodValue: Float?,
    val totalAverageMood: Mood?,
) {
    companion object {
        val Default: AppMetadata = AppMetadata(
            totalAverageMoodValue = null,
            totalAverageMood = null,
        )
    }


    val isEmpty: Boolean
        get() = totalAverageMood == null && totalAverageMoodValue == null

}