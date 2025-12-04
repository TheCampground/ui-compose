package dev.thecampground.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush

private const val SHADER_CODE = """
    uniform float2 resolution;
    uniform float time;
    
    // Simple hash function for pseudo-random value
    float random (in float2 st) {
        return fract(sin(dot(st.xy, float2(12.9898, 78.233))) * 43758.5453123);
    }
    
    // 2D Value Noise function
    float noise (in float2 st) {
        float2 i = floor(st);
        float2 f = fract(st);
    
        // 4 corners of the grid cell
        float a = random(i);
        float b = random(i + float2(1.0, 0.0));
        float c = random(i + float2(0.0, 1.0));
        float d = random(i + float2(1.0, 1.0));
    
        // Smooth Interpolation (Cubic Hermite Curve)
        float2 u = f * f * (3.0 - 2.0 * f);
    
        // Mix the corner values
        return mix(a, b, u.x) + 
               (c - a) * u.y * (1.0 - u.x) + 
               (d - b) * u.x * u.y;
    }
    
    // Fractional Brownian Motion (fBM) for organic look
    float fbm (in float2 st) {
        float value = 0.0;
        float amplitude = 0.5;
        // Sum 4 octaves of noise
        for (int i = 0; i < 4; i++) {
            value += amplitude * noise(st);
            st *= 2.0;           // Double frequency
            amplitude *= 0.5;    // Halve amplitude
        }
        return value;
    }
    
    // Main fragment shader entry point
    half4 main(float2 fragCoord) {
        // Normalize coordinates (0 to 1) and scale for zoom
        float2 uv = fragCoord / resolution.xy;
        float2 st = uv * 8.0; // Zoom factor
        
        // Add time to coordinates for gentle animation
        st += time * 0.1;
    
        float n = fbm(st);
    
        // Output a grayscale color based on the noise value
        return half4(n, n, n, 1.0);
    }
"""

@Composable
fun Noise() {

}