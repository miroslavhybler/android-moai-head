@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)
@file:Suppress(
    "DATA_CLASS_COPY_VISIBILITY_WILL_BE_CHANGED_WARNING",
    "RedundantVisibilityModifier"
)

package mir.oslav.moaihead.compose

import androidx.annotation.FloatRange
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.graphics.shapes.toPath
import androidx.wear.compose.material3.MaterialTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mir.oslav.moaihead.PreviewWearOS
import moaihead.data.Mood
import moaihead.ui.MoaiHeadTheme
import moaihead.ui.MoodColorScheme
import moaihead.ui.getMoodColorScheme
import moaihead.ui.getPrimaryColors
import moaihead.ui.shapes
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random


/**
 * Holding data for current abstract visualization of picked [Mood], doesn't affecting the picking,
 * just the UI of the [CircularMoodPicker].
 */
private class MoodVisualisationState internal constructor(
    private val initialContainerColor: Color,
    private val density: Density,
) {

    internal data class UIElement internal constructor(
        val shape: Path,
        val center: Offset,
        val size: Size,
        val color: Color,
    ) {

        val animationSet = AnimationSet()

        var isValid: Boolean = true

        val isInvalid: Boolean
            get() = !isValid
    }


    private var lastMood: Mood? = null


    /**
     * For smoother animations there can be shapes for multiple moods. When mood changes, old shapes
     * are animated to not to be visible and then removed.
     */
    val elements: Map<Mood, SnapshotStateList<UIElement>> = buildMap {
        Mood.entries.forEach {
            this[it] = SnapshotStateList()
        }
    }

    /**
     * Container (background) color of whole [CircularMoodPicker] selected by current mood and drag
     * progress to the edge.
     */
    val containerColor: Animatable<Color, AnimationVector4D> = Animatable(
        initialValue = initialContainerColor
    )


    /**
     *
     */
    val strokeSizes: Map<Mood, Animatable<Float, AnimationVector1D>> = Mood.entries.associateWith(
        valueSelector = {
            Animatable(initialValue = with(receiver = density) { 16f.dp.toPx() })
        }
    )


    suspend fun update(
        mood: Mood?,
        moodColorScheme: MoodColorScheme?,
        @FloatRange(from = 0.0, to = 1.0) progress: Float,
        size: IntSize,
        coroutineScope: CoroutineScope,
    ): Unit = with(receiver = coroutineScope) {

        if (mood == null || moodColorScheme == null) {
            animateToInitialState()
            return
        }

        if (moodColorScheme.container != containerColor.targetValue) {
            launch {
                containerColor.animateTo(
                    targetValue = moodColorScheme.container.copy(alpha = progress)
                )
            }
        }

        if (lastMood != mood) {
            val numberOfShapes = Random.nextInt(from = 3, until = 6)

            elements[lastMood]?.let { list ->
                list.removeAll(predicate = UIElement::isInvalid)
                if (list.isNotEmpty()) {
                    list.forEach {
                        it.animationSet.animateDisappear(coroutineScope = coroutineScope)
                        it.isValid = false
                    }
                }
            }


            val shapes = mood.shapes()
            //TODO validate on position and size
            for (i in 0 until numberOfShapes) {
                val shape = shapes.random()
                val center = Offset(
                    x = Random.nextInt(from = 0, until = size.width).toFloat(),
                    y = Random.nextInt(from = 0, until = size.height).toFloat(),
                )
                val dimen = Random.nextInt(
                    from = (size.width * 0.1f).toInt(),
                    until = (size.width * 0.9f).toInt()
                ).toFloat()

                val matrix = Matrix().apply {
                    translate(
                        x = (size.width * 0.5f) - (dimen * 0.5f),
                        y = (size.height * 0.5f) - (dimen * 0.5f),
                    )
                    scale(x = dimen, y = dimen)
                }

                val newElement = UIElement(
                    shape = shape.toPath().asComposePath().also { path ->
                        path.transform(matrix = matrix)
                    },
                    center = center,
                    size = Size(width = dimen, height = dimen),
                    color = moodColorScheme.primary.copy(
                        alpha = Random.nextInt(
                            from = 0,
                            until = 100
                        ) * 0.01f
                    ),
                )
                elements[mood]?.add(element = newElement)

                newElement.animationSet.animateAppear(coroutineScope = coroutineScope)
            }

            lastMood = mood
        }

        strokeSizes.forEach { (m, a) ->
            val target = with(receiver = density) { if (m == mood) 28f.dp.toPx() else 12.dp.toPx() }
            if (a.targetValue != target) {
                coroutineScope.launch {
                    a.animateTo(targetValue = target)
                }
            }
        }
    }


    /**
     *
     */
    private suspend fun CoroutineScope.animateToInitialState() {
        launch {
            containerColor.animateTo(targetValue = initialContainerColor)
        }
        val strokeSize = with(receiver = density) {
            if (lastMood != null) 8f.dp.toPx() else 16f.dp.toPx()
        }
        strokeSizes.forEach { (m, a) ->
            if (a.targetValue != strokeSize) {
                launch {
                    a.animateTo(targetValue = strokeSize)
                }
            }
        }
        elements.forEach { (mood, list) ->
            list.clear()
        }
    }
}


private const val START_ANGLE: Float = 270f


/**
 * Circular picker for [Mood] designer for round WearOS Display
 * @author Miroslav Hýbler <br>
 * created on 11.11.2025
 */
@Composable
fun CircularMoodPicker(
    modifier: Modifier = Modifier,
    onPicked: (Mood?) -> Unit,
) {
    val density = LocalDensity.current
    val materialColorScheme = MaterialTheme.colorScheme
    val textMeasurer = rememberTextMeasurer()
    val coroutineScope = rememberCoroutineScope()
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val visualState = remember {
        MoodVisualisationState(
            initialContainerColor = materialColorScheme.background,
            density = density,
        )
    }

    val moodColors = remember { getPrimaryColors(isDark = isSystemInDarkTheme) }

    var tempPickedMood: Mood? by remember { mutableStateOf(value = null) }
    var dragOffset by remember { mutableStateOf(value = Offset.Zero) }


    var progress by remember { mutableFloatStateOf(value = 0f) }


    val emojiTextStyle = TextStyle(fontSize = 42.sp, lineHeight = 45.sp)
    val titleTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = materialColorScheme.onBackground
    )


    val emojiTextResult = remember(key1 = tempPickedMood) {
        textMeasurer.measure(
            text = tempPickedMood?.emoji ?: Mood.MOAI.emoji,
            style = emojiTextStyle
        )
    }
    val moodNameTextResult = remember(key1 = tempPickedMood) {
        textMeasurer.measure(
            text = tempPickedMood?.name ?: Mood.MOAI.emoji,
            style = titleTextStyle
        )
    }


    val pointerX = remember { Animatable(initialValue = -1f) }
    val pointerY = remember { Animatable(initialValue = -1f) }
    var canvasSize by remember { mutableStateOf(value = IntSize.Zero) }


    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .then(other = modifier)
            .pointerInput(key1 = Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        dragOffset = dragOffset.copy(
                            x = change.position.x + dragAmount.x,
                            y = change.position.y + dragAmount.y
                        )
                        coroutineScope.launch {
                            pointerX.snapTo(targetValue = dragOffset.x)
                            pointerY.snapTo(targetValue = dragOffset.y)

                            //TODO include inner radius to enable "cancel"
                            //TODO support both layout directions
                            val index = getClosestItemByAngle(
                                x = pointerX.value,
                                y = pointerY.value,
                                cx = canvasSize.width * 0.5f,
                                cy = canvasSize.height * 0.5f,
                                startAngle = START_ANGLE,
                                sweepPerItem = 360f / Mood.entries.size,
                                itemCount = 10,
                            )
                            progress = getPointerEdgeProgress(
                                touchX = pointerX.value,
                                touchY = pointerY.value,
                                centerX = canvasSize.width * 0.5f,
                                centerY = canvasSize.height * 0.5f,
                                outerRadius = canvasSize.width * 0.5f,
                                innerRadius = canvasSize.width * 0.05f,
                            )

                            tempPickedMood =
                                if (index != -1 && progress > 0.1f) Mood.entries[index] else null
                            val newMood = tempPickedMood

                            visualState.update(
                                mood = newMood,
                                moodColorScheme = newMood?.let {
                                    getMoodColorScheme(
                                        mood = it,
                                        isDark = isSystemInDarkTheme
                                    )
                                },
                                progress = progress,
                                size = size,
                                coroutineScope = coroutineScope,
                            )
                        }

                        change.consume()
                    },
                    onDragEnd = {
                        tempPickedMood?.let(block = onPicked)
                        tempPickedMood = null

                        coroutineScope.launch {
                            pointerX.animateTo(targetValue = size.width / 2f)
                        }
                        coroutineScope.launch {
                            pointerY.animateTo(targetValue = size.height / 2f)
                        }


                        coroutineScope.launch {
                            visualState.update(
                                mood = null,
                                moodColorScheme = null,
                                progress = 0f,
                                size = size,
                                coroutineScope = coroutineScope,
                            )
                        }
                    }
                )
            }
            .onSizeChanged(
                onSizeChanged = { newSize ->
                    canvasSize = newSize
                    coroutineScope.launch {
                        pointerX.snapTo(targetValue = newSize.width / 2f)
                        pointerY.snapTo(targetValue = newSize.height / 2f)
                    }
                }
            ),
    ) {

        drawRect(color = visualState.containerColor.value)

        val sweepAngle = 360f / Mood.entries.size
        var startAngle = START_ANGLE

        //Effects must be behind circular picker so the picker is fully visible
        visualState.elements
            .flatMap(transform = Map.Entry<Mood, SnapshotStateList<MoodVisualisationState.UIElement>>::value)
            .fastForEachIndexed { index, item ->
                withTransform(
                    transformBlock = {
                        translate(
                            left = item.center.x - item.size.width / 2f,
                            top = item.center.y - item.size.height / 2f,
                        )
                        scale(scale = item.animationSet.scale.value)
                        rotate(degrees = item.animationSet.rotation.value)
                    },
                    drawBlock = {
                        drawPath(
                            path = item.shape,
                            color = item.color,
                            style = Fill,
                        )
                    },
                )
            }


        //Circular picker
        moodColors.fastForEachIndexed { index, color ->
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(
                    width = visualState.strokeSizes[Mood.entries[index]]!!.value,
                    cap = StrokeCap.Round,
                    miter = 4f,
                ),
            )
            startAngle += sweepAngle
        }

        if (tempPickedMood != null) {
            val topY =
                (canvasSize.height / 2) - (emojiTextResult.size.height / 2f) - (moodNameTextResult.size.height / 2f)

            drawText(
                textLayoutResult = emojiTextResult,
                topLeft = Offset(
                    x = (canvasSize.width / 2) - (emojiTextResult.size.width / 2f),
                    y = topY,
                ),
                alpha = progress,
            )

            drawText(
                textLayoutResult = moodNameTextResult,
                topLeft = Offset(
                    x = (canvasSize.width / 2) - (moodNameTextResult.size.width / 2f),
                    y = topY + emojiTextResult.size.height,
                ),
                alpha = progress,
            )
        }

        if (pointerX.value != -1f && pointerY.value != -1f) {
            drawCircle(
                color = materialColorScheme.onBackground,
                radius = 18.dp.toPx(),
                center = Offset(x = pointerX.value, y = pointerY.value),
            )
        }
    }
}


/**
 * Computes the index of the item whose sweep angle is closest to the given touch point.
 *
 * @param x Touch x coordinate
 * @param y Touch y coordinate
 * @param cx Center x coordinate of the circle
 * @param cy Center y coordinate of the circle
 * @param startAngle Starting angle of the first item, in degrees
 * @param sweepPerItem Sweep (angle span) of each item, in degrees
 * @param itemCount Number of items on the circle
 */
private fun getClosestItemByAngle(
    x: Float,
    y: Float,
    cx: Float,
    cy: Float,
    startAngle: Float,
    sweepPerItem: Float,
    itemCount: Int
): Int {
    // Compute angle from center to touch point
    val dx = x - cx
    val dy = y - cy
    var angle = Math.toDegrees(atan2(y = dy, x = dx).toDouble()).toFloat()

    // Normalize to [0, 360)
    if (angle < 0f) angle += 360f

    // Adjust relative to start angle
    val relativeAngle = (angle - startAngle + 360f) % 360f

    // Compute index
    val index = (relativeAngle / sweepPerItem)
        .roundToInt()
        .coerceIn(
            minimumValue = -1,
            maximumValue = itemCount - 1
        )

    return index
}


private fun getPointerEdgeProgress(
    touchX: Float,
    touchY: Float,
    centerX: Float,
    centerY: Float,
    innerRadius: Float,
    outerRadius: Float
): Float {
    val dx = touchX - centerX
    val dy = touchY - centerY
    val distance = sqrt(x = dx * dx + dy * dy)

    // Map the distance into 0f..1f between inner and outer radius
    return ((distance - innerRadius) / (outerRadius - innerRadius))
        .coerceIn(minimumValue = 0f, maximumValue = 1f)
}


@Composable
@PreviewWearOS
private fun CircularMoodPickerPreview() {
    MoaiHeadTheme() {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularMoodPicker(
                onPicked = {},
            )
        }
    }
}