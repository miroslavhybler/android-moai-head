package mir.oslav.moaihead

import android.app.Application
import androidx.wear.tiles.TileService
import dagger.hilt.android.HiltAndroidApp
import mir.oslav.moaihead.tile.MoodTileService


/**
 * @author Miroslav Hýbler <br>
 * created on 10.11.2025
 */
@HiltAndroidApp
class MoaiHeadApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //Just for debug
        TileService.getUpdater(this)
            .requestUpdate(MoodTileService::class.java)
    }
}