#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform float Strength;
uniform float ShadowWeight;
uniform float Saturation;
uniform float Contrast;
uniform float Vignette;

out vec4 fragColor;

float luma(vec3 color) {
    return dot(color, vec3(0.2126, 0.7152, 0.0722));
}

void main() {
    vec4 source = texture(DiffuseSampler, texCoord);
    vec3 color = source.rgb;

    float brightness = luma(color);
    float shadowMask = pow(1.0 - clamp(brightness, 0.0, 1.0), 1.55);
    color *= 1.0 - shadowMask * ShadowWeight * Strength;

    color = (color - 0.5) * Contrast + 0.5;

    float gray = luma(color);
    color = mix(vec3(gray), color, Saturation);

    vec3 warmHighlights = vec3(1.03, 1.00, 0.94);
    vec3 coolShadows = vec3(0.90, 0.95, 1.04);
    color *= mix(coolShadows, warmHighlights, smoothstep(0.25, 0.95, brightness));

    vec2 centered = texCoord * 2.0 - 1.0;
    float edge = smoothstep(0.20, 1.20, dot(centered, centered));
    color *= 1.0 - edge * Vignette * Strength;

    fragColor = vec4(clamp(color, 0.0, 1.0), source.a);
}
