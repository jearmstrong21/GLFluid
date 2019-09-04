#version 410 core

in vec2 uv;
uniform vec2 texSize;

uniform sampler2D divergence, last_pressure;

out vec4 fc;

vec4 s(sampler2D t,vec2 u){
    return texture(t,u/texSize);
}

void main(){
    fc=vec4(s(divergence,uv)+s(last_pressure,uv+vec2(2,0))+s(last_pressure,uv+vec2(-2,0))+s(last_pressure,uv+vec2(0,2))+s(last_pressure,uv+vec2(0,-2)))/4.0;
}