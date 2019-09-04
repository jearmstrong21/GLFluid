#version 410 core

in vec2 uv;

uniform vec2 texSize;
uniform vec2 texelSize;

uniform sampler2D density;
uniform sampler2D velocityX, velocityY;

out vec4 fc;

float g(int X,int Y){
    return dot(vec3(0.299, 0.587, 0.114),texture(density,vec2(uv.x+float(X),uv.y+float(Y))/texSize).xyz);
}

void main(){
    fc=texture(density, uv/texSize);
        float dx=g(1,0)-g(-1,0);dx/=2.0;
        float dy=g(0,1)-g(0,-1);dy/=2.0;
//    fc=vec4(normalize(vec3(abs(dx),abs(dy),0.5)),1.0);
//        fc=vec4(normalize(dx-dy),1.0);
}