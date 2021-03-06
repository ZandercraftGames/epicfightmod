#version 150

in vec2 pass_textureCoord;
in float brightness;
in float fogFactor;

out vec4 out_Color;

uniform vec3 lightColor;
uniform vec3 fogColor;
uniform sampler2D texture2D;

void main(void)
{
    out_Color = vec4(brightness, brightness, brightness, 1.0) * vec4(lightColor, 1.0) * texture(texture2D, pass_textureCoord);
    float alpha = out_Color.w;
    out_Color = mix(vec4(fogColor, 1.0), out_Color, fogFactor);
    out_Color = vec4(out_Color.xyz, alpha);
}