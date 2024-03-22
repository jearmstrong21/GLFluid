#version 410 core

in vec2 uv;
out vec4 fc;
uniform sampler2D source;
uniform vec2 texSize;

void main(){
    fc=texture(source,uv);
}