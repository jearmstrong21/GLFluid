#version 410 core

in vec2 uv;

uniform vec2 texSize;

uniform sampler2D density;
uniform sampler2D velocityX, velocityY;

out vec4 fc;

void main(){
//    fc=vec4(texture())
    fc=texture(density,uv/texSize);
    if(fc.x<0.0||fc.y<0.0)fc.z=1.0;
}