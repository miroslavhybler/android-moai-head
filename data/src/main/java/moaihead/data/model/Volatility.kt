package moaihead.data.model


/**
 * @author Miroslav Hýbler <br>
 * created on 03.01.2026
 */
enum class Volatility private constructor(
    val maxValue: Float,
) {
    Stable(maxValue = 1.5f),
    GentlyVariable(maxValue = 3f),
    ModeratelyVariable(maxValue = 5f),
    VeryVariable(maxValue = 10f);


    companion object {

        fun getFor(value: Float): Volatility {
            return entries.firstOrNull(predicate = { it.maxValue >= value }) ?: Stable
        }
    }
}