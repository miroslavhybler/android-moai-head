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
    val mostFrequentMood: Mood?,
    val volatility: Volatility?,
    val volatilityValue: Float?,
) {
    companion object {
        val Default: AppMetadata = AppMetadata(
            mostFrequentMood = null,
            volatility = null,
            volatilityValue = null,
        )
    }


    val isEmpty: Boolean
        get() = mostFrequentMood == null
                && volatility == null
                && volatilityValue == null


}