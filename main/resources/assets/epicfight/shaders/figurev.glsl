#version 400 core

in vec3 in_position;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main(void)
{
	gl_Position = projectionMatrix * viewMatrix * vec4(in_position, 1.0);
}