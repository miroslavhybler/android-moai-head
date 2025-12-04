@file:OptIn(ExperimentalMaterial3Api::class)

package mir.oslav.moaihead.ui.insert

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mir.oslav.moaihead.R
import mir.oslav.moaihead.compose.PreviewUI
import moaihead.data.model.Mood
import moaihead.ui.MoaiHeadTheme


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@Composable
fun InsertEntryScreen(
    onBack: () -> Unit,
    viewModel: InsertViewModel = viewModel(),
) {
    InsertEntryScreenImpl(
        onInsert = { mood, note ->
            viewModel.insert(mood = mood, note = note)
            onBack()
        },
        onBack = onBack,
    )
}


@Composable
private fun InsertEntryScreenImpl(
    onBack: () -> Unit,
    onInsert: (mood: Mood, note: String?) -> Unit,
) {
    var selectedMood: Mood? by remember { mutableStateOf(value = null) }

    BackHandler(enabled = selectedMood != null) {
        selectedMood = null
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Insert new entry",
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
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .verticalScroll(state = rememberScrollState())
                    .padding(paddingValues = innerPadding),
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            ) {
                Mood.entries.forEach { mood ->
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .clip(shape = MaterialTheme.shapes.medium)
                            .clickable(onClick = { selectedMood = mood })
                            .background(
                                color = if (selectedMood == mood)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    Color.Unspecified,
                            )
                            .padding(
                                horizontal = 12.dp,
                                vertical = 12.dp,
                            ),
                        horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = mood.emoji)

                        Column(modifier = Modifier.weight(weight = 1f)) {
                            Text(text = mood.name)
                            Text(text = mood.description)
                        }
                    }
                }
            }

        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 56.dp),
                    onClick = {
                        if (selectedMood != null) {
                            onInsert(selectedMood!!, null)
                        }
                    },
                ) {
                    Text(text = "Insert")
                }
            }
        },
    )
}


@Composable
@PreviewUI
private fun InsertEntryScreenPreview() {
    MoaiHeadTheme() {
        InsertEntryScreenImpl(
            onBack = {},
            onInsert = { _, _ -> },
        )
    }
}

