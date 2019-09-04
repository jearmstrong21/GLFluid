#version 410 core

in vec2 uv;
uniform vec2 texSize;

uniform sampler2D source, velocity;

out vec4 fc;

void main(){
    fc=texture(source, uv/texSize+texture(velocity,uv/texSize).xy/texSize.x*0.1  );
}