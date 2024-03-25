#version 410 core

in vec2 uv;

out vec4 fc;

void main(){
    vec2 u=uv;//*4.0*3.1415;
//    fc=vec4(cos(u.x),sin(u.y),cos(u.y-1.0)*sin(u.x),0.0);
    fc=vec4(0);
    fc.z=1.25;
    //if((int(u.x*5.0)+int(u.y*5.0))%5==0)fc.z=2.0; else fc.z=0.75;
    //if((int(u.x*10.0)+int(u.y*10.0)+1)%8==0)fc.w=1.0;
}