package mir.oslav.moaihead.utils

import android.content.Context
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import moaihead.data.utils.awaitResult


/**
 * @author Miroslav Hýbler <br>
 * created on 19.12.2025
 */
suspend fun Context.tryGetConnectedWearOS(): Node? {
    val nodeClient = Wearable.getNodeClient(this)
    val nodesResult = nodeClient.connectedNodes.awaitResult()
    return nodesResult.getOrNull()?.firstOrNull()
}