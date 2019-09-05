#version 410 core

in vec2 uv;

uniform vec2 texSize;
uniform vec2 texelSize;

uniform sampler2D density;
uniform sampler2D velocityX, velocityY;

out vec4 fc;

vec3 g(int X,int Y){
    return texture(density,vec2(uv.x+float(X),uv.y+float(Y))/texSize).xyz;
}


float g1(int X,int Y){
    return length(g(X,Y));
}

void main(){
//    fc=texture(density, uv/texSize);
            float dx1=g1(1,0)-g1(-1,0);dx1/=2.0;
            float dy1=g1(0,1)-g1(0,-1);dy1/=2.0;
        fc=vec4(normalize(vec3(abs(dx1),abs(dy1),0.5)),1.0);
//            vec3 dx=g(1,0)-g(-1,0);dx/=2.0;
//            vec3 dy=g(0,1)-g(0,-1);dy/=2.0;
//        fc=vec4(normalize(dx+dy),1.0);
}