#version 410 core

in vec2 uv;

uniform vec2 texSize;

uniform sampler2D density;
uniform sampler2D velocityX, velocityY;

out vec4 fc;

vec3 g(int X,int Y){
    return texture(density,vec2(uv.x+float(X),uv.y+float(Y))/texSize).xyz;
}

void main(){
//    fc=vec4(texture())
    fc=texture(density,uv/texSize);
//    vec3 dx=g(1,0)-g(-1,0);dx/=2.0;
//    vec3 dy=g(0,1)-g(0,-1);dy/=2.0;
//    fc=vec4(normalize(vec3(dx.x,dy.x,0.0000001)),1.0);
}