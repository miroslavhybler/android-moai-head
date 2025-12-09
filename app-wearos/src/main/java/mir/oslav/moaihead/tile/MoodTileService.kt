@file:OptIn(ExperimentalHorologistApi::class)

package mir.oslav.moaihead.tile

import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileBuilders.Tile
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import dagger.hilt.android.AndroidEntryPoint
import moaihead.data.model.AppMetadata
import mir.oslav.moaihead.data.MetadataRepo
import moaihead.data.model.Mood
import moaihead.room.LocalDatabaseRepo
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@AndroidEntryPoint
class MoodTileService constructor() : SuspendingTileService() {


    companion object {
        const val RESOURCES_VERSION: String = "0"
    }


    @Inject
    lateinit var repo: MetadataRepo


    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest,
    ): ResourceBuilders.Resources {
        return ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .build()
    }


    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest,
    ): Tile {
        val metadata = repo.metadata.value

        return Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                Timeline.fromLayoutElement(
                    tileLayout(
                        this,
                        requestParams.deviceConfiguration,
                        metadata = metadata,
                    )
                )
            )
            .build()
    }
}