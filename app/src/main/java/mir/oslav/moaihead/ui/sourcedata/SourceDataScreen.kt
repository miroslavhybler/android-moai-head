@file:OptIn(ExperimentalMaterial3Api::class)

package mir.oslav.moaihead.ui.sourcedata

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mockup.Mockup
import mir.oslav.moaihead.compose.PreviewUI
import mir.oslav.moaihead.ui.Route
import moaihead.data.MoodEntry
import moaihead.data.PlainMoodEntry


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@Composable
fun SourceDataScreen(
    onNavigate: (Route) -> Unit,
    viewModel: SourceDataViewModel = viewModel(),
) {
    val data = viewModel.repo.moodData.collectAsState()

    SourceDataScreenImpl(
        data = data.value,
        onNavigate = onNavigate,
    )
}


@Composable
private fun SourceDataScreenImpl(
    data: List<MoodEntry>,
    onNavigate: (Route) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Source Data",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding),
            ) {
                items(
                    items = data,
                    key = { item -> item.timestamp.epochSecond }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { onNavigate(Route.MoodEntry(entry = it)) })
                            .padding(vertical = 4.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = it.mood.emoji)

                        Text(
                            text = it.mood.name,
                            modifier = Modifier.weight(weight = 1f),
                            style = MaterialTheme.typography.titleMedium,
                        )

                        Text(
                            text = it.timestamp.toString(),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }

        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp),
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 56.dp),
                    onClick = {
                        onNavigate(Route.InsertEntry)
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
private fun SourceDataScreenPreview() {
    SourceDataScreenImpl(
        data = Mockup.plainMoodEntry.list.map(transform = PlainMoodEntry::toMoodEntry),
        onNavigate = { _ -> },
    )
}