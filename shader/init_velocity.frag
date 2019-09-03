#version 410 core

in vec2 uv;

uniform vec2 texSize;

out vec4 fc;

void main(){
    float f=4.0;
    fc=vec4(sin(uv.y/texSize.x*6.2831853072*f),sin(uv.x/texSize.x*6.2831853072*f),0.0,1.0);
//    fc=vec4( -normalize(uv-texSize/2.0),0.0,1.0  );
    fc.w=1.0;
}