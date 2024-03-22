#version 410 core

in vec2 uv;
uniform vec2 texSize;

uniform sampler2D source, velocity;

out vec4 fc;

uniform float dt;

void main(){
    vec2 x = uv;
    vec2 v = texture(velocity, x).xy;
    fc=texture(source, x - v * dt);
}