package mir.oslav.moaihead.utils

import moaihead.data.model.Mood
import moaihead.data.model.MoodEntry
import moaihead.data.model.Volatility
import kotlin.math.abs

/**
 * @author Miroslav Hýbler <br>
 * created on 08.12.2025
 */

fun getMostFrequentMood(
    entries: List<MoodEntry>,
): Mood? {
    if (entries.isEmpty()) {
        return null
    }
    return entries.groupBy(keySelector = MoodEntry::mood)
        .maxByOrNull(selector = { it.value.size })
        ?.key
}


fun calculateVolatility(
    entries: List<MoodEntry>,
): Pair<Volatility?, Float?> {
    if (entries.size < 2) {
        return null to null
    }

    val changes = entries
        .sortedBy(selector = { it.timestamp })
        .zipWithNext(transform = { a, b ->
            abs(n = a.mood.value - b.mood.value)
        })

    val volatilityValue = changes.average().toFloat()
    val volatility = Volatility.getFor(value = volatilityValue)
    return volatility to volatilityValue
}