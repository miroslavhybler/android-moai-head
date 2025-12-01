package moaihead.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import moaihead.data.Mood


/**
 * @author Miroslav Hýbler <br>
 * created on 13.11.2025
 */
data class MoodColorScheme(
    val primary: Color,
    val onPrimary: Color,
    val container: Color,
    val onContainer: Color
)


private val LightMoodColorSchemes: Map<Mood, MoodColorScheme> = mapOf(
    Mood.MOAI to MoodColorScheme(
        primary = Color(color = 0xFF607D8B), // Slate gray-blue
        onPrimary = Color(color = 0xFFFFFFFF),
        container = Color(color = 0xFFB0BEC5),
        onContainer = Color(color = 0xFF263238)
    ),
    Mood.JOYFUL to MoodColorScheme(
        primary = Color(color = 0xFFFFEB3B),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFFFFF59D),
        onContainer = Color(color = 0xFF3E2723)
    ),
    Mood.EXCITED to MoodColorScheme(
        primary = Color(color = 0xFFFF9800),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFFFFCC80),
        onContainer = Color(color = 0xFF4E342E)
    ),
    Mood.CALM to MoodColorScheme(
        primary = Color(color = 0xFF4CAF50),
        onPrimary = Color(color = 0xFFFFFFFF),
        container = Color(color = 0xFFA5D6A7),
        onContainer = Color(color = 0xFF1B5E20)
    ),
    Mood.NEUTRAL to MoodColorScheme(
        primary = Color(color = 0xFFE0E0E0),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFFF5F5F5),
        onContainer = Color(color = 0xFF212121)
    ),
    Mood.TIRED to MoodColorScheme(
        primary = Color(color = 0xFF81D4FA),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFFB3E5FC),
        onContainer = Color(color = 0xFF01579B)
    ),
    Mood.STRESSED to MoodColorScheme(
        primary = Color(color = 0xFF2196F3),
        onPrimary = Color(color = 0xFFFFFFFF),
        container = Color(color = 0xFF90CAF9),
        onContainer = Color(color = 0xFF0D47A1)
    ),
    Mood.FRUSTRATED to MoodColorScheme(
        primary = Color(color = 0xFFF44336),
        onPrimary = Color(color = 0xFFFFFFFF),
        container = Color(color = 0xFFEF9A9A),
        onContainer = Color(color = 0xFFB71C1C)
    ),
    Mood.SAD to MoodColorScheme(
        primary = Color(color = 0xFF9C27B0),
        onPrimary = Color(color = 0xFFFFFFFF),
        container = Color(color = 0xFFCE93D8),
        onContainer = Color(color = 0xFF4A148C)
    ),
    Mood.LONELY to MoodColorScheme(
        primary = Color(color = 0xFFB39DDB),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFFD1C4E9),
        onContainer = Color(color = 0xFF311B92)
    )
)

/* ---------- DARK MODE ---------- */

private val DarkMoodColorSchemes: Map<Mood, MoodColorScheme> = mapOf(
    Mood.MOAI to MoodColorScheme(
        primary = Color(color = 0xFF90A4AE),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFF37474F),
        onContainer = Color(color = 0xFFECEFF1)
    ),
    Mood.JOYFUL to MoodColorScheme(
        primary = Color(color = 0xFFFFEE58),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFFFBC02D),
        onContainer = Color(color = 0xFF212121)
    ),
    Mood.EXCITED to MoodColorScheme(
        primary = Color(color = 0xFFFFB74D),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFFF57C00),
        onContainer = Color(color = 0xFFFFF3E0)
    ),
    Mood.CALM to MoodColorScheme(
        primary = Color(color = 0xFF81C784),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFF388E3C),
        onContainer = Color(color = 0xFFE8F5E9)
    ),
    Mood.NEUTRAL to MoodColorScheme(
        primary = Color(color = 0xFF9E9E9E),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFF616161),
        onContainer = Color(color = 0xFFECECEC)
    ),
    Mood.TIRED to MoodColorScheme(
        primary = Color(color = 0xFF4FC3F7),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFF0288D1),
        onContainer = Color(color = 0xFFE1F5FE)
    ),
    Mood.STRESSED to MoodColorScheme(
        primary = Color(color = 0xFF64B5F6),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFF1565C0),
        onContainer = Color(color = 0xFFE3F2FD)
    ),
    Mood.FRUSTRATED to MoodColorScheme(
        primary = Color(color = 0xFFE57373),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFFC62828),
        onContainer = Color(color = 0xFFFFEBEE)
    ),
    Mood.SAD to MoodColorScheme(
        primary = Color(color = 0xFFBA68C8),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFF6A1B9A),
        onContainer = Color(color = 0xFFF3E5F5)
    ),
    Mood.LONELY to MoodColorScheme(
        primary = Color(color = 0xFFB39DDB),
        onPrimary = Color(color = 0xFF000000),
        container = Color(color = 0xFF512DA8),
        onContainer = Color(color = 0xFFEDE7F6)
    )
)


@Composable
fun moodColorScheme(
    mood: Mood,
    isDark: Boolean = isSystemInDarkTheme(),
): MoodColorScheme {
    return getMoodColorScheme(
        mood = mood,
        isDark = isDark,
    )
}


fun getPrimaryColors(isDark: Boolean): List<Color> {
    return if (isDark) {
        DarkMoodColorSchemes.values.map(transform = MoodColorScheme::primary)
    } else {
        LightMoodColorSchemes.values.map(transform = MoodColorScheme::primary)
    }
}


fun getMoodColorScheme(
    mood: Mood,
    isDark: Boolean,
): MoodColorScheme {
    return if (isDark) {
        DarkMoodColorSchemes[mood] ?: DarkMoodColorSchemes[Mood.NEUTRAL]!!
    } else {
        LightMoodColorSchemes[mood] ?: LightMoodColorSchemes[Mood.NEUTRAL]!!
    }
}
