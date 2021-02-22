#version 150

const int MAX_JOINTS = 22;
const int MAX_WEIGHTS = 3;
const vec3 light0 = normalize(vec3(0.20000000298023224, 1.0, -0.699999988079071));
const vec3 light1 = normalize(vec3(-0.20000000298023224, 1.0, 0.699999988079071));

in vec3 in_position;
in vec3 in_normal;
in vec2 in_textureCoord;
in ivec3 in_jointIndices;
in vec3 in_weights;

out vec2 pass_textureCoord;
out float brightness;
out float fogFactor;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 jointTransforms[MAX_JOINTS];
uniform float fogMin;
uniform float fogMax;
uniform float fogDensity;
uniform int fogOperation;

void main(void)
{
    vec4 totalLocalPos = vec4(0.0);
    vec4 totalNormal = vec4(0.0);
    
    for(int i = 0; i < MAX_WEIGHTS; i++)
    {
        mat4 jointTransform = jointTransforms[in_jointIndices[i]];
        vec4 posePosition = jointTransform * vec4(in_position, 1.0);
        totalLocalPos += posePosition * in_weights[i];
        
        vec4 worldNormal = jointTransform * vec4(in_normal, 0.0);
        totalNormal += worldNormal * in_weights[i];
    }
    
    vec4 posToCamera = viewMatrix * modelMatrix * totalLocalPos;
    gl_Position = projectionMatrix * posToCamera;
    
    mat4 modelRotationMatrix = modelMatrix;
    modelRotationMatrix[0][3] = 0.0;
    modelRotationMatrix[1][3] = 0.0;
    modelRotationMatrix[2][3] = 0.0;
    modelRotationMatrix[3][3] = 1.0;
    totalNormal = modelRotationMatrix * totalNormal;
    
    float dot1 = max(dot(totalNormal.xyz, light0), 0.2);
    float dot2 = max(dot(totalNormal.xyz, light1), 0.2);
    brightness = min((dot1 + dot2), 1.0);
    
    float distance = length(posToCamera.xyz);
    
    if(fogOperation == 0)
    {
    	fogFactor = (fogMax - distance)/(fogMax - fogMin);
    }
    else
    {
    	fogFactor = exp(-pow(fogDensity * distance, fogOperation));
    }
    
    fogFactor = clamp(fogFactor, 0.0, 1.0);
    pass_textureCoord = in_textureCoord;
}