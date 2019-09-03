#version 410 core

in vec2 uv;

uniform vec2 texSize;

out vec4 fc;

void main(){
    fc=vec4(sign( mod(floor(uv.x/texSize.x*10.0)+floor(uv.y/texSize.x*10.0) ,2.0)  ));
//    fc=vec4(1,0,0,1);
    fc.w=1.0;
}