#version 410 core

in vec2 uv;
uniform vec2 texSize;

uniform sampler2D divergence, last_pressure;
//uniform sampler2D velocity;
uniform float dt;
out vec4 fc;

vec4 s(sampler2D t,vec2 u,vec2 o){
    return texture(t,u+o/texSize);
}

//vec2 v(int x,int y){
//    return texture(velocity, uv + vec2(x*2, y*2) / texSize).xy;
//}

void main(){
    fc=vec4(s(divergence,uv,vec2(0))+s(last_pressure,uv,vec2(2,0))+s(last_pressure,uv,vec2(-2,0))+s(last_pressure,uv,vec2(0,2))+s(last_pressure,uv,vec2(0,-2)))/4.0;
}