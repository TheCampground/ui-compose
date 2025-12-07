package dev.thecampground.ui

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush

@Composable
fun produceDrawLoopCounter(speed: Float = 1f): State<Float> {
    return produceState(0f) {
        val firstFrame: Long = withFrameMillis { it }
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = (it - firstFrame) * speed / 1000f
            }
        }
    }
}

@Composable
fun SketchWithCache(
    modifier: Modifier = Modifier,
    speed: Float = 1f,
    onDrawWithCache: CacheDrawScope.(time: Float) -> DrawResult
) {
    val time by produceDrawLoopCounter(speed)
    Box(
        modifier = modifier.drawWithCache {
            onDrawWithCache(time)
        }
    )
}


val Coral = Color(0xFFF3A397)
val LightYellow = Color(0xFFF8EE94)

@Language(value = "AGSL")
val NoiseGrain2Android =
    RuntimeShader(
        """
uniform float2 resolution;
uniform float time;
layout(color) uniform half4 baseColor;
layout(color) uniform half4 baseColor2; 
//uniform float grainSize;
//
//float random(vec2 p) {
//vec2 K1 = vec2(
//23.14069263277926,
//2.665144142690225
//);
//return fract(cos(dot(p, K1)) * 43758.5453);
//}
half4 main(in float2 fragCoord) {
//// normalized pixel coords
float2 uv = fragCoord / resolution.xy;
float mixValue = distance(uv, vec2(0, 1));

//half4 color = half4(baseColor);
//// stable grain (no shifting)
//float g = random(uv + time * 0.00001); // time optional
//
//color += g * intensity * grainSize;

return mix(baseColor, baseColor2, mixValue * (time));

}
    """.trimIndent()
    )

/**
 * TODO: Get the android noise generation working..
 *  Right now it doesn't :(
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
actual fun Noise(intensity: Float, animated: Boolean, bg: Color) {
    val shader2 = remember { ShaderBrush(NoiseGrain2Android) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SketchWithCache(speed = 1f, modifier = Modifier.fillMaxSize()) { time ->
            NoiseGrain2Android.setFloatUniform("resolution", size.width, size.height)
            NoiseGrain2Android.setFloatUniform("time", time)
            NoiseGrain2Android.setColorUniform(
                "baseColor",
                android.graphics.Color.valueOf(
                    LightYellow.red,
                    LightYellow.green,
                    LightYellow.blue,
                    LightYellow.alpha
                )
            )
            NoiseGrain2Android.setColorUniform(
                "baseColor2",
                android.graphics.Color.valueOf(Coral.red, Coral.green, Coral.blue, Coral.alpha)
            )
            onDrawBehind {
                drawRect(brush = shader2)
            }
        }
    }
}