@file:OptIn(ExperimentalHorologistApi::class)

package mir.oslav.moaihead.tile

import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService

/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
class MoodTileService : SuspendingTileService() {


    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest,
    ): ResourceBuilders.Resources {
        TODO("Not yet implemented")
    }

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest,
    ): TileBuilders.Tile {
        TODO("Not yet implemented")
    }
}