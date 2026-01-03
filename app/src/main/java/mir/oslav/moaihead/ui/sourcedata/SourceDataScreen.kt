
package mir.oslav.moaihead.ui.sourcedata

import android.widget.Toast
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.wearable.Wearable
import com.mockup.Mockup
import mir.oslav.moaihead.R
import mir.oslav.moaihead.compose.PreviewUI
import mir.oslav.moaihead.ui.Route
import moaihead.data.Endpoints
import moaihead.data.model.MoodEntry
import moaihead.data.model.PlainMoodEntry
import moaihead.data.model.SyncState


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceDataScreen(
    onNavigate: (Route) -> Unit,
    viewModel: SourceDataViewModel = viewModel(),
) {
    val context = LocalContext.current
    val data = viewModel.repo.moodData.collectAsState()
    val localToRemoteSyncState by viewModel.syncState.collectAsState()


    LaunchedEffect(key1 = localToRemoteSyncState) {
        if (localToRemoteSyncState is SyncState.Unknown) {
            return@LaunchedEffect
        }

        Toast.makeText(
            context,
            when (localToRemoteSyncState) {
                is SyncState.SyncFinished ->
                    if ((localToRemoteSyncState as SyncState.SyncFinished).isSuccess) {
                        "Sync successful"
                    } else {
                        "Sync failed"
                    }

                is SyncState.SyncRunning -> "Sync into Firestore running"
                else -> ""
            },
            Toast.LENGTH_SHORT
        ).show()
    }


    SourceDataScreenImpl(
        data = data.value,
        onNavigate = onNavigate,
        onRequestWearOSSync = {
            Wearable.getNodeClient(context).connectedNodes
                .addOnSuccessListener { nodes ->
                    nodes.firstOrNull()?.let { wearNode ->
                        val messageClient = Wearable.getMessageClient(context)

                        messageClient.sendMessage(
                            wearNode.id,
                            Endpoints.FromPhoneToWear.REQUEST_WEAR_SYNC,
                            byteArrayOf()
                        ).addOnSuccessListener { a ->
                            Toast.makeText(
                                context,
                                "Synchronizing data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                            .addOnFailureListener { exception ->
                                exception.printStackTrace()
                                Toast.makeText(
                                    context,
                                    "Request failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    Toast.makeText(
                        context,
                        "Unable to get nodes",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        },
        syncState = localToRemoteSyncState,
        onLocalToRemoteSyncRequested = viewModel::requestLocalToRemoteSync,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SourceDataScreenImpl(
    data: List<MoodEntry>,
    onNavigate: (Route) -> Unit,
    onRequestWearOSSync: () -> Unit,
    syncState: SyncState,
    onLocalToRemoteSyncRequested: () -> Unit,
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
                actions = {

                    IconButton(onClick = onLocalToRemoteSyncRequested) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sync),
                            contentDescription = null,
                        )
                    }



                    IconButton(onClick = onRequestWearOSSync) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_wear),
                            contentDescription = null,
                        )
                    }
                }
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
                    key = { item -> item.timestamp.toEpochMilli() }
                ) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { onNavigate(Route.MoodEntry(entry = item)) })
                            .padding(vertical = 4.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = item.mood.emoji)

                        Text(
                            text = item.mood.name,
                            modifier = Modifier.weight(weight = 1f),
                            style = MaterialTheme.typography.titleMedium,
                        )

                        Text(
                            text = item.timestamp.toString(),
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
                    Text(text = "Insert New Entry")
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
        onRequestWearOSSync = {},
        syncState = SyncState.Unknown,
        onLocalToRemoteSyncRequested = {},

        )
}
