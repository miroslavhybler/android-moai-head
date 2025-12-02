@file:OptIn(ExperimentalMaterial3Api::class)

package mir.oslav.moaihead.ui.entry

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mir.oslav.moaihead.compose.PreviewUI
import mir.oslav.moaihead.ui.Route
import moaihead.data.EntrySource
import moaihead.data.Mood
import moaihead.data.MoodEntry
import mir.oslav.moaihead.R
import moaihead.ui.MoaiHeadTheme
import java.time.Instant


/**
 * @author Miroslav Hýbler <br>
 * created on 02.12.2025
 */
@Composable
fun EntryScreen(
    onBack: () -> Unit,
    route: Route.MoodEntry,
    viewModel: EntryViewModel = viewModel(),
) {

    EntryScreenImpl(
        onBack = onBack,
        entry = route.entry,
        onSaveNewNote = { note ->
            viewModel.updateNote(
                note = note,
                entry = route.entry,
            )
            onBack()
        },
        onDelete = {
            viewModel.delete(entry = route.entry)
            onBack()
        }
    )
}


@Composable
private fun EntryScreenImpl(
    onBack: () -> Unit,
    entry: MoodEntry,
    onSaveNewNote: (note: String?) -> Unit,
    onDelete: () -> Unit,
) {
    var note by remember { mutableStateOf(value = entry.note ?: "") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Entry detail",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .clickable(onClick = onBack)
                            .padding(all = 8.dp),
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null,
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(paddingValues = paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 12.dp),
            ) {

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
                ) {
                    Text(
                        text = "Mood: ${entry.mood.emoji}",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(
                        text = entry.mood.name,
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }

                Text(
                    text = entry.mood.description,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = entry.timestamp.toString(),
                    style = MaterialTheme.typography.labelSmall,
                )


                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(text = "Note") },
                    maxLines = 5,
                    minLines = 1,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    shape = MaterialTheme.shapes.medium,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences,
                    )
                )
            }
        },
        bottomBar = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(space = 12.dp),
            ) {

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 56.dp),
                    onClick = onDelete,
                ) {
                    Text(text = "Delete")
                }


                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 56.dp),
                    onClick = { onSaveNewNote(note) },
                ) {
                    Text(text = "Save")
                }
            }
        },
    )
}


@Composable
@PreviewUI
private fun EntryScreenPreview() {
    MoaiHeadTheme {

        EntryScreenImpl(
            onBack = {},
            entry = MoodEntry(
                mood = Mood.entries.random(),
                timestamp = Instant.ofEpochMilli(System.currentTimeMillis()),
                note = "Had a great meal!",
                source = EntrySource.UserInitiative,
            ),
            onSaveNewNote = { _ -> },
            onDelete = { },
        )
    }
}