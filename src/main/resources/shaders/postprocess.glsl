#version 330

uniform sampler2D u_text;
uniform vec2 u_resolution;
uniform sampler2D textureSampler;

out vec4 fragColor;

void main() {
  vec2 uv = gl_FragCoord.xy / u_resolution.xy;

  vec4 color = texture2D(textureSampler, uv);

  fragColor = vec4(color.xzy, 1.0);
}
