package mir.oslav.moaihead.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random


/**
 * @param rotation Rotation around z axis
 * @author Miroslav Hýbler <br>
 * created on 19.11.2025
 */
data class AnimationSet constructor(
    val scaleEnterAnimationSpec: FiniteAnimationSpec<Float> = tween(
        durationMillis = 800,
        easing = EaseOutBack,
    ),
    val scaleExitAnimationSpec: FiniteAnimationSpec<Float> = tween(durationMillis = 300),
    val rotationEnterAnimationSpec: FiniteAnimationSpec<Float> = tween(durationMillis = 700),
    val rotationExitAnimationSpec: FiniteAnimationSpec<Float> = tween(durationMillis = 400)
) {

    val scale: Animatable<Float, AnimationVector1D> = Animatable(initialValue = 0f)
    val rotation: Animatable<Float, AnimationVector1D> = Animatable(initialValue = 0f)


    var isVisible: Boolean = false
        private set




    fun animateAppear(
        coroutineScope: CoroutineScope,
        targetScale: Float = 1f,
        targetRotation: Float = Random.nextInt(from = 0, until = 360).toFloat(),
    ): Unit = with(receiver = coroutineScope) {
        launch {
            scale.animateTo(
                targetValue = targetScale,
                animationSpec = scaleEnterAnimationSpec
            )
        }

        launch {
            rotation.animateTo(
                targetValue = targetRotation,
                animationSpec = rotationEnterAnimationSpec
            )
        }
    }



    fun animateDisappear(
        coroutineScope: CoroutineScope,
        targetScale: Float = 0f,
        targetRotation: Float = Random.nextInt(from = 0, until = 360).toFloat(),
    ): Unit = with(receiver = coroutineScope) {
        launch {
            scale.animateTo(
                targetValue = targetScale,
                animationSpec = scaleExitAnimationSpec,
            )
        }

        launch {
            rotation.animateTo(
                targetValue = targetRotation,
                animationSpec = rotationExitAnimationSpec,
            )
        }
    }
}