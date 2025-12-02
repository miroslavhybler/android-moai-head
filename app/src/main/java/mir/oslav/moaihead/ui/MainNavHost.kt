package mir.oslav.moaihead.ui

import androidx.annotation.Keep
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import mir.oslav.moaihead.ui.entry.EntryScreen
import mir.oslav.moaihead.ui.insert.InsertEntryScreen
import mir.oslav.moaihead.ui.sourcedata.SourceDataScreen


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@Composable
fun MainNavHost() {

    val backstack = rememberNavBackStack(Route.SourceData)

    NavDisplay(
        modifier = Modifier
            .fillMaxSize()
            .semantics(
                properties = { testTagsAsResourceId = true }
            ),
        backStack = backstack,
        onBack = { backstack.pop() },
        entryProvider = entryProvider(
            fallback = {
                error(message = "Unknown route: $it")
            },
            builder = {
                entry<Route.SourceData> {
                    SourceDataScreen(
                        onNavigate = backstack::add,
                    )
                }

                entry<Route.InsertEntry> {
                    InsertEntryScreen(
                        onBack = backstack::pop,
                    )
                }
                entry<Route.MoodEntry> { route ->
                    EntryScreen(
                        onBack = backstack::pop,
                        route = route,
                    )
                }
            }
        ),

        predictivePopTransitionSpec = {
            //Out transition when user is doing "back" navigation gesture
            val outT = slideOutHorizontally(
                targetOffsetX = { width -> width * 50 / 100 }
            ) + fadeOut(targetAlpha = 0.75f)

            //In transition for the underlying screen
            val inT = fadeIn()

            inT togetherWith outT
        }
    )
}


sealed class Route private constructor() : NavKey {

    @Keep
    @Serializable
    data object InsertEntry : Route()

    @Keep
    @Serializable
    data object SourceData : Route()

    @Keep
    @Serializable
    data class MoodEntry constructor(
        val entry: moaihead.data.MoodEntry,
    ) : Route()
}


private fun NavBackStack<*>.pop() {
    if (this.size > 1) {
        this.removeLastOrNull()
    }
}