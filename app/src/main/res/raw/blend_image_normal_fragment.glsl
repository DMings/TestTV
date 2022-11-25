precision mediump float;
uniform sampler2D uBaseTexture;
uniform sampler2D uBlendTexture;
uniform float uAlpha;
varying vec2 vBaseTexCoord;
varying vec2 vBlendTexCoord;

void main() {
    vec4 baseColor = texture2D(uBaseTexture, vBaseTexCoord);
    vec4 blendColor = texture2D(uBlendTexture, vBlendTexCoord);
    vec4 color = baseColor * (1.0 - blendColor.a) + blendColor * blendColor.a * uAlpha;
    if (color.a == 1.0){
        gl_FragColor = vec4(clamp(color.rgb / color.a, 0.0, 1.0), 1.0);
    } else {
        gl_FragColor = vec4(clamp(color.rgb / color.a, 0.0, 1.0), color.a);
    }
}