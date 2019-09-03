#version 410 core

uniform sampler2D density;
uniform sampler2D velocity;

in vec2 uv;
uniform vec2 texSize;

out vec4 fc;

vec4 get_density(vec2 u){
    return texture(density,u/texSize);
}

vec2 get_velocity(vec2 u){
    return texture(velocity,u/texSize).xy;
}

void main(){
    fc=get_density(  uv+get_velocity(uv) );
//    fc=get_density(uv+vec2(1.0,0.0));
//    fc=get_density(uv);
}