package mir.oslav.moaihead

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material3.MaterialTheme
import moaihead.ui.DarkColorScheme
import moaihead.ui.LightColorScheme


/**
 * @author Miroslav Hýbler <br>
 * created on 19.12.2025
 */
@Composable
fun MoaiHeadWearOsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}