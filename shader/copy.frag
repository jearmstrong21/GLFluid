#version 410 core

in vec2 uv;
out vec4 fc;
uniform sampler2D source;

void main(){
    fc=texture(source,uv);
}