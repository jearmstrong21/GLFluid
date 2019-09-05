#version 410 core

in vec2 uv;
uniform vec2 texSize;

uniform float BLUR_DIST;

uniform sampler2D source;
uniform float IS_HORIZONTAL;
uniform float OMEGA;

out vec4 fc;

void main(){
    vec3 total=vec3(0);
    float sum=0.0;

//    for(float x=-BLUR_DIST;x<=BLUR_DIST;x++){
//        for(float y=-BLUR_DIST;y<=BLUR_DIST;y++){
//            float weight=1.0;
//            sum+=weight;
//            total+=weight*texture(source,vec2(uv.x+x,uv.y+y)/texSize).xyz;
//        }
//    }
    for(float i=-BLUR_DIST;i<=BLUR_DIST;i++){
        vec2 u=vec2(uv);
        if(IS_HORIZONTAL>0.0){
            u.x+=i;
        }else{
            u.y+=i;
        }
        u/=texSize;
        float weight=0.3989422804/OMEGA*exp(-i*i/(2.0*OMEGA*OMEGA));
        sum+=weight;
        total+=weight*texture(source,u).xyz;
    }

    fc=vec4(total/sum,1);
//    fc=texture(source,uv/texSize);
}