package mir.oslav.moaihead.utils

import android.content.Context
import android.widget.Toast
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import moaihead.data.utils.awaitResult


/**
 * @author Miroslav Hýbler <br>
 * created on 09.12.2025
 */
suspend fun Context.tryGetConnectedPhone(): Node? {
    val nodeClient = Wearable.getNodeClient(this)
    val nodesResult = nodeClient.connectedNodes.awaitResult()
   return nodesResult.getOrNull()?.firstOrNull()
}