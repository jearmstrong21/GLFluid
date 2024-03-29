#version 410 core

in vec2 uv;
uniform vec2 texSize;

out vec4 fc;

uniform vec2 mouseDir;
uniform vec2 mousePos;

uniform sampler2D velocity;

void main(){
    vec2 vel=texture(velocity,uv).xy;
    float d=length(mousePos-uv*texSize)*0.01;
    if(d<500)vel+=mouseDir*exp(-10.0*d*d);
    //if(length(mousePos-uv)<30)vel+=mouseDir*4.0;
//    if(length(mousePos-uv)<50)vel-=3.0*normalize(mousePos-uv);
//    vel=normalize(mousePos-uv)*4.0;

    fc=vec4(vel,0,1);
}