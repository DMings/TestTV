uniform mat4 uMVPMatrix;
uniform mat4 uBaseTexMatrix;
uniform mat4 uBlendTexMatrix;
attribute vec4 aPosition;
attribute vec4 aTextureCoord;
varying vec2 vBaseTexCoord;
varying vec2 vBlendTexCoord;

void main () {
    gl_Position = uMVPMatrix * aPosition;
    vBaseTexCoord = (uBaseTexMatrix * aTextureCoord).xy;
    vBlendTexCoord = (uBlendTexMatrix * aTextureCoord).xy;
}