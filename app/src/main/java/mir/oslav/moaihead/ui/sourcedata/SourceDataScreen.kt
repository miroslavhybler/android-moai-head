package mir.oslav.moaihead.ui.sourcedata

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mir.oslav.moaihead.compose.PreviewUI
import mir.oslav.moaihead.ui.Route
import moaihead.data.MoodRecord


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@Composable
fun SourceDataScreen(
    onNavigate: (Route) -> Unit,
    viewModel: SourceDataViewModel = viewModel(),
) {
    val data = viewModel.repo.allData.collectAsState()

    SourceDataScreenImpl(
        data = data.value,
        onNavigate = onNavigate,
    )
}


@Composable
private fun SourceDataScreenImpl(
    data: List<MoodRecord>,
    onNavigate: (Route) -> Unit,
) {
    Scaffold(
        topBar = {},
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding),
            ) {
                items(
                    items = data,
                    key = { it.timestamp.epochSecond }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
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
                    .padding(horizontal = 20.dp)
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
        data = emptyList(),
        onNavigate = { _ -> },
    )
}