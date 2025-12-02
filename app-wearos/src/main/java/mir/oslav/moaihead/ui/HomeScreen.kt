package mir.oslav.moaihead.ui

import androidx.compose.runtime.Composable
import mir.oslav.moaihead.compose.CircularMoodPicker


/**
 * @author Miroslav Hýbler <br>
 * created on 02.12.2025
 */
@Composable
fun HomeScreen(
    onNavigate: (route: String) -> Unit,
) {

    CircularMoodPicker(
        onPicked = { pickedMood ->
            if (pickedMood != null) {
                onNavigate("new-entry/${pickedMood.value}")
            }
        }
    )
}