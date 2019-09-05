#version 410 core

in vec2 uv;
uniform vec2 texSize;

uniform sampler2D source;

uniform float THRESHOLD_LEN;

out vec4 fc;

void main(){
    vec3 f=texture(source,uv/texSize).xyz;
    if(dot(f,f)<THRESHOLD_LEN*THRESHOLD_LEN)f=vec3(0);
    fc=vec4(f,1);
}