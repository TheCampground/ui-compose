package dev.thecampground.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

fun ByteArray.putFloatLE(pos: Int, value: Float) {
    val bits = value.toBits()
    this[pos] = (bits and 0xFF).toByte()
    this[pos + 1] = ((bits shr 8) and 0xFF).toByte()
    this[pos + 2] = ((bits shr 16) and 0xFF).toByte()
    this[pos + 3] = ((bits shr 24) and 0xFF).toByte()
}

@Language(value = "AGSL")
val NoiseGrain2 =
    """
uniform float2 resolution;
uniform float intensity;
uniform float time; // optional - for animation
uniform float3 baseColor;
uniform float grainSize;

float random(vec2 p) {
vec2 K1 = vec2(
23.14069263277926,
2.665144142690225
);
return fract(cos(dot(p, K1)) * 43758.5453);
}
vec4 main(vec2 coord) {
// normalized pixel coords
vec2 uv = coord / resolution;
// base color (black)
vec3 color = vec3(baseColor);

// stable grain (no shifting)
float g = random(uv + time * 0.00001); // time optional

color += g * intensity * grainSize;

return vec4(color, 1.0);

}
    """.trimIndent()

/**
 *
 */
annotation class Language(val value: String)

// TODO: Fix animated state
@Composable
expect fun Noise(intensity: Float = 0.8f, animated: Boolean = false, bg: Color)