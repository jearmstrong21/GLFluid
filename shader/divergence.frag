#version 410 core

in vec2 uv;
uniform vec2 texSize;
uniform sampler2D velocity;

out vec4 fc;

vec2 vel(int X,int Y){
    X*=2;
    Y*=2;
    return texture(velocity,(uv+vec2(X,Y))/texSize).xy;
}

#define RHO 1.0

void main(){
    float d=vel(1,0).x-vel(-1,0).x+vel(0,1).y-vel(0,-1).y;
    fc=vec4(-2.0/texSize.x*RHO*d);
}