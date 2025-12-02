package mir.oslav.moaihead.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import mir.oslav.moaihead.compose.SubmitMoodLayout
import moaihead.data.Mood


/**
 * @author Miroslav Hýbler <br>
 * created on 02.12.2025
 */
@Composable
fun NewEntryScreen(
    onBack: () -> Unit,
    mood: Mood,
    onSubmitMoodEntry: (Mood) -> Unit,
) {

    ScreenScaffold(
        scrollState = remember {
            TransformingLazyColumnState()
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(paddingValues = paddingValues)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
                ) {
                    Text(
                        text = mood.emoji,
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Column() {
                        Text(
                            text = mood.name,
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(
                            text = mood.description,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .offset(y = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(
                        space = 12.dp,
                        alignment = Alignment.CenterVertically,
                    )
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 32.dp),
                        onClick = onBack,
                    ) {
                        Text(
                            text = "Cancel",
                        )
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 32.dp),
                        onClick = { onSubmitMoodEntry(mood) },
                    ) {
                        Text(
                            text = "Submit",
                        )
                    }

                }
            }
        },
    )
}