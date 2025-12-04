package mir.oslav.moaihead.tile

import android.content.Context
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textEdgeButton
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.tooling.preview.Preview
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import androidx.wear.tooling.preview.devices.WearDevices


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
fun tileLayout(
    context: Context,
    deviceConfiguration: DeviceParametersBuilders.DeviceParameters,
    message: String,
): LayoutElementBuilders.LayoutElement {
    return materialScope(
        context = context,
        deviceConfiguration = deviceConfiguration,
        allowDynamicTheme = false,
    ) {
        primaryLayout(
            mainSlot = {
                text(text = message.layoutString)
            },
            bottomSlot = {
                textEdgeButton(
                    onClick = clickable(),
                    labelContent = { text(text = "Mood".layoutString) }
                )
            }
        )
    }
}


//Tiles preview, NOT COMPOSE PREVIEW!
@Preview(device = WearDevices.SMALL_ROUND, name = "Small Round")
@Preview(device = WearDevices.LARGE_ROUND, name = "Large Round")
private fun helloLayoutPreview(context: Context): TilePreviewData {
    return TilePreviewData {
        TilePreviewHelper.singleTimelineEntryTileBuilder(
            layoutElement = tileLayout(
                context = context,
                deviceConfiguration = it.deviceConfiguration,
                message = "Hello, preview tile!"
            )
        )
            .build()
    }
}