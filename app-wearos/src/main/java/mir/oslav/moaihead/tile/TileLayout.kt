package mir.oslav.moaihead.tile

import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.wear.protolayout.ActionBuilders.launchAction
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DimensionBuilders.DpProp
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Column
import androidx.wear.protolayout.LayoutElementBuilders.Row
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.material3.Typography
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
import mir.oslav.moaihead.MainActivity
import mir.oslav.moaihead.R
import moaihead.data.model.AppMetadata
import moaihead.data.model.Mood
import moaihead.ui.getMoodColorScheme
import java.util.Locale


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
fun tileLayout(
    context: Context,
    deviceConfiguration: DeviceParametersBuilders.DeviceParameters,
    metadata: AppMetadata,
): LayoutElementBuilders.LayoutElement {

    val colors = getMoodColorScheme(
        mood = metadata.totalAverageMood ?: Mood.NEUTRAL,
        isDark = true,
    )

    return materialScope(
        context = context,
        deviceConfiguration = deviceConfiguration,
        allowDynamicTheme = false,
    ) {
        val onClick = launchAction(
            ComponentName(
                context,
                MainActivity::class.java
            )
        )

        primaryLayout(
            titleSlot = {
                text(
                    text = context.getString(R.string.app_name).layoutString
                )
            },
            mainSlot = {
                Column.Builder()
                    .setModifiers(
                        ModifiersBuilders.Modifiers.Builder()
                            .setBackground(
                                ModifiersBuilders.Background.Builder()
                                    .setBrush(
                                        ColorBuilders.LinearGradient.Builder(
                                            ColorBuilders.ColorProp.Builder(colors.primary.toArgb())
                                                .build(),
                                            ColorBuilders.ColorProp.Builder(colors.container.toArgb())
                                                .build(),

                                            ).build()
                                    )
                                    .setCorner(
                                        ModifiersBuilders.Corner.Builder()
                                            .setRadius(DpProp.Builder(24f).build())
                                            .build()
                                    )
                                    .build()
                            )
                            .setPadding(
                                ModifiersBuilders.Padding.Builder()
                                    .setStart(dp(16f))
                                    .setEnd(dp(16f))
                                    .setTop(dp(12f))
                                    .setBottom(dp(12f))
                                    .build()
                            )
                            .build()
                    )
                    .apply {
                        addContent(
                            text(
                                text = "${metadata.totalAverageMood?.emoji} ${metadata.totalAverageMood?.name}".layoutString,
                                typography = Typography.TITLE_LARGE,
                            )

                        )
                        addContent(
                            text(
                                text = "Average: ${
                                    String.format(
                                        Locale.getDefault(),
                                        "%.1f",
                                        metadata.totalAverageMoodValue
                                    )
                                }".layoutString,
                                typography = Typography.LABEL_SMALL,
                            )
                        )
                    }
                    .build()
            },
            bottomSlot = {
                textEdgeButton(
                    onClick = clickable(action = onClick),
                    labelContent = { text(text = "New Entry".layoutString) },
                )
            },
            onClick = clickable(action = onClick)
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
                metadata = AppMetadata.Default,
            )
        )
            .build()
    }
}


//Tiles preview, NOT COMPOSE PREVIEW!
@Preview(device = WearDevices.SMALL_ROUND, name = "Small Round")
@Preview(device = WearDevices.LARGE_ROUND, name = "Large Round")
private fun helloLayoutPreview2(context: Context): TilePreviewData {
    return TilePreviewData {
        TilePreviewHelper.singleTimelineEntryTileBuilder(
            layoutElement = tileLayout(
                context = context,
                deviceConfiguration = it.deviceConfiguration,
                metadata = AppMetadata(
                    totalAverageMood = Mood.EXCITED,
                    totalAverageMoodValue = 8.2f,
                ),
            )
        )
            .build()
    }
}