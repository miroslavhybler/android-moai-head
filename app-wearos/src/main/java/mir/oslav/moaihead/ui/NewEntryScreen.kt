package mir.oslav.moaihead.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Picker
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.rememberPickerState
import mir.oslav.moaihead.compose.PreviewWearOS
import moaihead.data.model.EntrySource
import moaihead.data.model.Mood
import moaihead.data.model.MoodEntry
import moaihead.ui.MoaiHeadTheme
import java.time.Instant


/**
 * @author Miroslav Hýbler <br>
 * created on 02.12.2025
 */
@Composable
fun NewEntryScreen(
    onBack: () -> Unit,
    mood: Mood,
    viewModel: NewEntryViewModel = hiltViewModel(),
) {

    val activity = LocalActivity.current
    val notesForMood by viewModel.notesByMood.collectAsState()


    LaunchedEffect(key1 = mood) {
        viewModel.loadNotesForMood(mood = mood)
    }

    NewEntryScreenImpl(
        mood = mood,
        notesForMood = notesForMood,
        onSubmitMoodEntry = { mood, note ->
            if (activity != null) {
                viewModel.insert(
                    moodEntry = MoodEntry(
                        mood = mood,
                        timestamp = Instant.ofEpochMilli(System.currentTimeMillis()),
                        note = note,
                        source = EntrySource.UserInitiative,
                    ),
                    activity = activity,
                    onFinish = onBack
                )
            }
        },
    )
}

@Composable
private fun NewEntryScreenImpl(
    mood: Mood,
    notesForMood: List<Pair<String, Int>>,
    onSubmitMoodEntry: (mood: Mood, note: String?) -> Unit,
) {
    var note: String? by rememberSaveable { mutableStateOf(value = null) }
    val scrollState = remember { TransformingLazyColumnState() }

    ScreenScaffold(
        scrollState = scrollState,
        content = { paddingValues ->
            TransformingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState,
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.Top,
            ) {
                item {
                    Text(
                        text = mood.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }

                item(
                    key = Int.MIN_VALUE + 1,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
                    ) {
                        Text(
                            text = mood.emoji,
                            style = MaterialTheme.typography.displaySmall,
                        )
                        Column() {

                            Text(
                                text = mood.description,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }

                //TODO replace with picker in the future
                items(
                    items = notesForMood,
                    key = { it.first },
                ) { usedNote ->
                    FilterChip(
                        modifier = Modifier
                            .animateItem(),
                        selected = note == usedNote.first,
                        onClick = {
                            note = if (note != usedNote.first) usedNote.first else null
                        },
                        label = {
                            Text(text = "${usedNote.first} (${usedNote.second})")
                        },
                        shape = MaterialTheme.shapes.large,
                        contentPadding = PaddingValues(all = 10.dp)
                    )
                }
            }
        },
        edgeButton = {
            EdgeButton(
                onClick = { onSubmitMoodEntry(mood, note) }) {
                Text(
                    text = "Submit",
                )
            }
        }
    )
}


@Composable
@PreviewWearOS
private fun NewEntryScreenPreview() {
    MoaiHeadTheme() {
        NewEntryScreenImpl(
            mood = Mood.entries.first(),
            onSubmitMoodEntry = { _, _ -> },
            notesForMood = listOf(
                "Note 1" to 1,
                "Note 2" to 2,
            )
        )
    }
}