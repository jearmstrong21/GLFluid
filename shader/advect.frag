#version 410 core

in vec2 uv;
uniform vec2 texSize;

uniform sampler2D source, velocity;

out vec4 fc;

uniform float DT;

void main(){
    fc=texture(source, uv/texSize-0.5*texture(velocity,uv/texSize).xy/texSize.x*DT  );
}