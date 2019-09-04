#version 410 core

in vec2 uv;

out vec4 fc;

uniform vec2 texSize;

void main(){
//    fc=vec4(uv/texSize,0,1);
    float f=5.0;
    fc=vec4(  mod( floor(uv.x/texSize.x*f)+floor(uv.y/texSize.x*f),2.0  )  );
    fc.w=1.0;
//    fc=texture()
}