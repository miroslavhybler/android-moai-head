package moaihead.data


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
enum class EntrySource private constructor(
    val value: Int,
) {
    UserInitiative(value = 0),
    Scheduled(value = 1),


}