package mir.oslav.moaihead.data

import android.content.Context
import android.util.Log
import androidx.wear.tiles.TileService
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import mir.oslav.moaihead.tile.MoodTileService
import mir.oslav.moaihead.utils.tryGetConnectedPhone
import moaihead.data.Endpoints
import moaihead.data.model.AppMetadata
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Miroslav Hýbler <br>
 * created on 09.12.2025
 */
@Singleton
class MetadataRepo @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private val json: Json = Json {
            ignoreUnknownKeys = true
        }
    }

    private val metadataFile: File = File(
        context.filesDir,
        "metadata.json",
    )


    private val mMetadata: MutableStateFlow<AppMetadata> =
        MutableStateFlow(value = tryGetMetadataValue(file = metadataFile))
    val metadata: StateFlow<AppMetadata> = mMetadata.asStateFlow()


    init {
        if (mMetadata.value.isEmpty) {
            CoroutineScope(Dispatchers.IO).launch {
                context.tryGetConnectedPhone()?.let { phoneNode ->
                    //No need to react on success
                    //When operation is successful, WearOsListenerService will receive new metadata

                    Log.d("mirek", "WearOs - requesting metadata sync")

                    Wearable.getMessageClient(context).sendMessage(
                        phoneNode.id,
                        Endpoints.FromWearToPhone.REQUEST_METADATA_SYNC,
                        byteArrayOf(),
                    ).addOnFailureListener { exception ->
                        exception.printStackTrace()
                    }
                }
            }
        }
    }


    fun loadMetadata() {
        mMetadata.value = tryGetMetadataValue(file = metadataFile)
    }


    fun saveMetadata(metadata: AppMetadata) {
        if (!metadataFile.exists()) {
            metadataFile.createNewFile()
        }

        metadataFile.writeText(
            text = json.encodeToString(
                serializer = AppMetadata.serializer(),
                value = metadata,
            )
        )

        mMetadata.value = metadata

        //Metadata were updated, request update of Tile
        TileService.getUpdater(context)
            .requestUpdate(MoodTileService::class.java)
    }


    private fun tryGetMetadataValue(file: File): AppMetadata {
        if (!file.exists()) {
            return AppMetadata.Default
        }

        return try {
            val metadata = json.decodeFromString(
                deserializer = AppMetadata.serializer(),
                string = file.readText(),
            )
            metadata
        } catch (e: Exception) {
            e.printStackTrace()
            file.delete()
            AppMetadata.Default
        }
    }
}