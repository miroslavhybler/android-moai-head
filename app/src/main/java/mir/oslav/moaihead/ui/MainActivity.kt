package mir.oslav.moaihead.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import moaihead.ui.MoaiHeadTheme

/**
 * @author Miroslav Hýbler <br>
 * created on 10.11.2025
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MoaiHeadTheme {
                MainNavHost()
            }
        }
    }
}