#version 410 core

in vec2 uv;
uniform vec2 texSize;

uniform sampler2D density;

out vec4 fc;

uniform vec2 mousePos;
uniform float change;

void main(){
    vec4 f=texture(density,uv);

    float df=0.0;
    if(length(uv*texSize-mousePos)<30){
        df+=change/(length(uv*texSize-mousePos)+0.1);
    }
    f+=df;

    fc=f;
}