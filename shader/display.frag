#version 410 core

in vec2 uv;

uniform vec2 texSize;

uniform sampler2D density;
uniform sampler2D velocity;

out vec4 fc;

void main(){
    fc=texture(density,uv/texSize);
//    fc=texture(velocity,uv/texSize);
//    fc=vec4(1.0,0.0,0.0,1.0);
//    fc=vec4(uv/texSize,0.0,1.0);
}