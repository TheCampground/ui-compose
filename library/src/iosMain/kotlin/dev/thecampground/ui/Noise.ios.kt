package dev.thecampground.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import org.jetbrains.skia.Data
import org.jetbrains.skia.RuntimeEffect
import java.nio.ByteBuffer
import java.nio.ByteOrder

@Composable
actual fun Noise(intensity: Float, animated: Boolean, bg: Color) {
    val noiseEffect = remember {
        RuntimeEffect.makeForShader(NoiseGrain2)
    }
    val time = remember { mutableStateOf(20f) }
    val size = remember { mutableStateOf(Size.Zero) }
    // trigger animation
    val uniformBuffer =
        remember { ByteBuffer.allocate(4 * (2 + 1 + 1 + 3 + 1)).order(ByteOrder.LITTLE_ENDIAN) }
    if (animated) {
        LaunchedEffect(Unit) {
            while (animated) {
                withFrameNanos { nanos ->
                    time.value = nanos / 100_000_00f // seconds
                }
            }
        }
    }

    LaunchedEffect(time.value, size.value) {
        uniformBuffer.clear()
        uniformBuffer.putFloat(size.value.width)
        uniformBuffer.putFloat(size.value.height)
        uniformBuffer.putFloat(intensity)
        uniformBuffer.putFloat(time.value)
        uniformBuffer.putFloat(bg.red)
        uniformBuffer.putFloat(bg.green)
        uniformBuffer.putFloat(bg.blue)
        uniformBuffer.putFloat(1f)
        uniformBuffer.flip()
    }

    val noiseShader = remember(uniformBuffer, time.value) {

        noiseEffect.makeShader(
            uniforms = Data.makeFromBytes(uniformBuffer.array()),
            children = emptyArray(),  // no child shaders
            localMatrix = null,
        )
    }

    Canvas(Modifier.fillMaxSize()) {
        size.value = this.size
        drawRect(
            brush = ShaderBrush(noiseShader),
            alpha = intensity
        )
    }

}