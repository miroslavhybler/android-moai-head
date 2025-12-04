@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package moaihead.ui

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.graphics.shapes.RoundedPolygon
import moaihead.data.model.Mood

/**
 * @author Miroslav Hýbler <br>
 * created on 13.11.2025
 */
object MoodShapes {

    val Joyful = listOf(
        MaterialShapes.Clover4Leaf,
        MaterialShapes.SoftBurst
    )

    val Calm = listOf(
        MaterialShapes.Circle,
        MaterialShapes.PuffyDiamond
    )

    val Neutral = listOf(
        MaterialShapes.Pill,
        MaterialShapes.Circle
    )

    val Sad = listOf(
        MaterialShapes.ClamShell,
        MaterialShapes.Clover4Leaf
    )

    val Angry = listOf(
        MaterialShapes.Boom,
        MaterialShapes.Burst
    )

    val Stressed = listOf(
        MaterialShapes.Triangle,
        MaterialShapes.Boom
    )

    val Anxious = listOf(
        MaterialShapes.Diamond,
        MaterialShapes.Flower
    )

    val Relaxed = listOf(
        MaterialShapes.Clover8Leaf,
        MaterialShapes.SoftBoom
    )

    val Excited = listOf(
        MaterialShapes.Cookie12Sided,
        MaterialShapes.Burst
    )

    val Moai = listOf(
        MaterialShapes.Circle,
        MaterialShapes.Clover8Leaf
    )
}

/**
 * Extension function to retrieve the associated shape list for each mood.
 */
fun Mood.shapes(): List<RoundedPolygon> = when (this) {
    Mood.MOAI -> MoodShapes.Moai
    Mood.JOYFUL -> MoodShapes.Joyful
    Mood.EXCITED -> MoodShapes.Excited
    Mood.CALM -> MoodShapes.Calm
    Mood.NEUTRAL -> MoodShapes.Neutral
    Mood.TIRED -> MoodShapes.Anxious
    Mood.STRESSED -> MoodShapes.Stressed
    Mood.FRUSTRATED -> MoodShapes.Angry
    Mood.SAD -> MoodShapes.Sad
    Mood.LONELY -> MoodShapes.Relaxed
}
