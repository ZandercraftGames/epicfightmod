#version 400 core

out vec4 out_color;

uniform vec4 color;
uniform sampler2D texture;

void main(void)
{
	out_color = color;
}