#version 410 core

in vec2 uv;
uniform vec2 texSize;

uniform sampler2D tex;

out vec4 fc;

void main(){
    vec4 f=texture(tex,uv);
    if(f.x<0.0)f.x=0.0;
    if(f.y<0.0)f.y=0.0;
    if(f.z<0.0)f.z=0.0;
    if(f.w<0.0)f.w=0.0;
    fc=f;
}