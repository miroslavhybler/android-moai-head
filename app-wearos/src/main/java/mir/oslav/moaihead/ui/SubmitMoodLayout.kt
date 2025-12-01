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
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import moaihead.data.Mood


/**
 * @author Miroslav Hýbler <br>
 * created on 25.11.2025
 */
@Composable
fun SubmitMoodLayout(
    modifier: Modifier = Modifier,
    mood: Mood,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
) {

    ScreenScaffold(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(key1 = Unit, block = { detectTapGestures(onTap = {}) }),
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
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 48.dp),
                        onClick = onSubmit,
                    ) {
                        Text(
                            text = "Submit",
                        )
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 48.dp),
                        onClick = onCancel,
                    ) {
                        Text(
                            text = "Cancel",
                        )
                    }
                }
            }
        },
    )
}