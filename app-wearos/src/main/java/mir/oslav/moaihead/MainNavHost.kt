package mir.oslav.moaihead

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import mir.oslav.moaihead.ui.HomeScreen
import mir.oslav.moaihead.ui.NewEntryScreen
import moaihead.data.model.Mood


/**
 * @author Miroslav Hýbler <br>
 * created on 02.12.2025
 */
@Composable
fun MainNavHost() {
    val navController = rememberSwipeDismissableNavController()

    AppScaffold {
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = Route.HOME,
        ) {
            composable(route = Route.HOME) {
                HomeScreen(
                    onNavigate = { route ->
                        navController.navigate(route = route)
                    }
                )
            }
            composable(
                route = Route.NEW_ENTRY,
                arguments = listOf(
                    navArgument(name = "mood") {
                        type = NavType.IntType
                        nullable = false
                        defaultValue = Mood.CALM.value
                    }
                )
            ) { entry ->
                val mood = entry.arguments?.getInt("mood") ?: Mood.CALM.value
                NewEntryScreen(
                    mood = Mood.entries.first(predicate = { it.value == mood }),
                    onBack = { navController.navigateUp() },
                )
            }
        }
    }

}


object Route {

    const val HOME: String = "home"

    const val NEW_ENTRY: String = "new-entry/{mood}"


}