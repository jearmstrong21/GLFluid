#version 410 core

in vec2 uv;

uniform vec2 texSize;

uniform sampler2D tex;

out vec4 fc;

#define U (uv.x)
#define V (uv.y)

vec4 T(int x,int y){
    return texture(tex,(uv+vec2(float(x),float(y)))/texSize);
}

vec4 T(float x,float y){
    return texture(tex,(uv+vec2(x,y))/texSize);
}

void main(){
    fc=T(0,0);
//    fc = ( T(-5,0)+T(5,0)+T(0,-1)+T(0,1))/4.0;
//    fc=T(-1,0);
//    fc=mix(fc,sin(fc),0.1);
}