#version 410 core

in vec2 uv;

out vec4 fc;

uniform vec2 texSize;

void main(){
    vec2 u=uv*2.0*6.2831853072;
    float f=25.0;
    //fc=vec4(sin(u.y)*f,sin(u.x)*f,0,1)*10.0;
    fc=vec4(cos(u.y),cos(u.x),0,1)*10.0;
    fc+=vec4(cos(u.y+3.14),cos(u.x-3.14),0,1)*40.0;
    fc.z=0.0;
    fc.w=1.0;
}