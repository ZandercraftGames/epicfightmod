#version 150

in vec2 pass_textureCoord;
in float brightness;
in float fogFactor;

out vec4 out_Color;

uniform vec3 lightColor;
uniform vec3 fogColor;
uniform vec4 overlayColor;
uniform vec4 glintColor;
uniform mat4 glintTransform1;
uniform mat4 glintTransform2;
uniform int overlay;
uniform int glint;

uniform sampler2D texture2D;
uniform sampler2D overlay2D;
uniform sampler2D glint2D;

void main(void)
{
    vec4 totalFrag;
    vec4 textureFrag;
    
    if(overlay == 1)
    {
        vec4 overlayFrag = texture(overlay2D, pass_textureCoord);
        
        if(overlayFrag.w > 0)
        {
            textureFrag = overlayFrag;
        }
        else
        {
            textureFrag = overlayColor * texture(texture2D, pass_textureCoord);
        }
    }
    else
    {
        textureFrag = texture(texture2D, pass_textureCoord);
    }
    
    if(glint == 1)
    {
        vec4 texCoord = vec4(pass_textureCoord, 0, 1);
        vec4 glintCoord1 = glintTransform1 * texCoord;
        vec4 glintCoord2 = glintTransform2 * texCoord;
        vec2 glintTexCoord1 = vec2(glintCoord1.x, glintCoord1.y);
        vec2 glintTexCoord2 = vec2(glintCoord2.x, glintCoord2.y);
        
        vec4 glintFrag1 = glintColor * texture(glint2D, glintTexCoord1);
        vec4 glintFrag2 = glintColor * texture(glint2D, glintTexCoord2);
        
        if(textureFrag.w > 0)
        {
            totalFrag = vec4(textureFrag.x + (glintFrag1.x*glintFrag1.x) + (glintFrag2.x*glintFrag2.x),
            textureFrag.y + (glintFrag1.y*glintFrag1.y) + (glintFrag2.y*glintFrag2.y),
            textureFrag.z + (glintFrag1.z*glintFrag1.z) + (glintFrag2.z*glintFrag2.z),
            1.0);
        }
        else
        {
            totalFrag = textureFrag;
        }
    }
    else
    {
        totalFrag = textureFrag;
    }
    
    
    out_Color = vec4(brightness, brightness, brightness, 1.0) * vec4(lightColor, 1.0) * totalFrag;
    
    float alpha = out_Color.w;
    out_Color = mix(vec4(fogColor, 1.0), out_Color, fogFactor);
    out_Color = vec4(out_Color.xyz, alpha);
}