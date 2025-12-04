package mir.oslav.moaihead

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import dagger.hilt.android.AndroidEntryPoint
import moaihead.data.model.Mood
import moaihead.ui.MoaiHeadTheme
import moaihead.ui.moodColorScheme


/**
 * @author Miroslav Hýbler <br>
 * created on 11.11.2025
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)
        setContent {
            MoaiHeadTheme {
                MainNavHost()
            }
        }
    }
}


@Composable
fun Test() {
    val layoutDirection = LocalLayoutDirection.current

    var selectedMood: Mood? by remember { mutableStateOf(value = null) }

    BackHandler(enabled = selectedMood != null) {
        selectedMood = null
    }

    val scrollState = remember {
        TransformingLazyColumnState()
    }

    ScreenScaffold(
        scrollState = scrollState,
        timeText = {
            Text(text = "Time")
        },
        edgeButton = {
            EdgeButton(onClick = {}) {
                Text(text = "More")
            }
        },
    ) { paddingValues ->
        TransformingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
        ) {
            items(items = Mood.entries.toList()) { mood ->
                val colorScheme = moodColorScheme(mood = mood)
                Row(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .background(color = colorScheme.container)
                        .clickable(onClick = {
                            selectedMood = mood
                        })
                        .padding(
                            start = paddingValues.calculateStartPadding(layoutDirection = layoutDirection),
                            end = paddingValues.calculateStartPadding(layoutDirection = layoutDirection),
                            top = 12.dp,
                            bottom = 12.dp,
                        ),
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = colorScheme.primary,
                                shape = CircleShape
                            )
                            .padding(all = 4.dp)
                    ) {
                        Text(text = mood.emoji)
                    }
                    Text(
                        text = mood.name,
                        color = colorScheme.onContainer,
                    )
                }
            }
        }
    }
}