@file:Suppress("RedundantVisibilityModifier")

package moaihead.data


/**
 * @author Miroslav Hýbler <br>
 * created on 10.11.2025
 */
public enum class Mood private constructor(
    val emoji: String,
    val description: String,
    val value: Int,
) {
    MOAI(
        emoji = "🗿",
        description = "Feeling grounded, confident, and content with yourself",
        value = 10,
    ),
    JOYFUL(
        emoji = "😄",
        description = "Feeling happy and uplifted",
        value = 9,
    ),
    EXCITED(
        emoji = "🤩",
        description = "Feeling energetic and motivated",
        value = 8,
    ),
    CALM(
        emoji = "😌",
        description = "Feeling peaceful and at ease",
        value = 7,
    ),
    NEUTRAL(
        emoji = "😐",
        description = "Feeling okay, neither good nor bad",
        value = 6,
    ),
    TIRED(
        emoji = "😴",
        description = "Feeling low energy or sleepy",
        value = 5,
    ),
    STRESSED(
        emoji = "😰",
        description = "Feeling pressured or tense",
        value = 4,
    ),
    FRUSTRATED(
        emoji = "😠",
        description = "Feeling irritated or angry",
        value = 3,
    ),
    SAD(
        emoji = "😢",
        description = "Feeling down or disappointed",
        value = 2,
    ),
    LONELY(
        emoji = "🥺",
        description = "Feeling isolated or disconnected",
        value = 1,
    )
}
