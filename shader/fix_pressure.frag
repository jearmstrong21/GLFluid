#version 410 core

in vec2 uv;
uniform vec2 texSize;

uniform sampler2D velocity,pressure;

out vec4 fc;

uniform float RHO;
#define DT 0.1
uniform float EPSILON;

float p(int X,int Y){
    return texture(pressure,uv+vec2(X,Y)/texSize).x;
}

void main(){
    vec2 vel=texture(velocity,uv).xy;
    vel.x-=DT/(2.0*RHO*EPSILON)*(p(1,0)-p(-1,0));
    vel.y-=DT/(2.0*RHO*EPSILON)*(p(0,1)-p(0,-1));
    fc=vec4(vel,0,1);
}