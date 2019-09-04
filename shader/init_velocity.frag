#version 410 core

in vec2 uv;

out vec4 fc;

uniform vec2 texSize;

void main(){
    vec2 u=uv/texSize.x*5.0*6.2831853072;
    float f=5.0;
    fc=vec4(sin(u.y)*f,sin(u.x)*f,0,1);
//    fc=vec4(1,sin(u.y),0,1);
}