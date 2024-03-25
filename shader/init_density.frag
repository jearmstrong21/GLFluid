//#version 410 core
//
//in vec2 uv;
//
//out vec4 fc;
//
//uniform vec2 texSize;
//
//void main(){
////    fc=vec4(uv/texSize,0,1);
//    float f=5.0;
//    fc=vec4(  mod( floor(uv.x/texSize.x*f)+floor(uv.y/texSize.x*f),2.0  )  );
//    fc.w=1.0;
////    fc=texture()
//}

#version 410 core
#define TWO_PI 6.28318530718

uniform vec2 texSize;

//  Function from Iñigo Quiles
//  https://www.shadertoy.com/view/MsS3Wc
vec3 hsb2rgb(in vec3 c){
    vec3 rgb = clamp(abs(mod(c.x*6.0+vec3(0.0, 4.0, 2.0),
    6.0)-3.0)-1.0,
    0.0,
    1.0);
    rgb = rgb*rgb*(3.0-2.0*rgb);
    return c.z * mix(vec3(1.0), rgb, c.y);
}

in vec2 uv;
out vec4 fc;

float checkerboard() {
    float t=50.0;
    return float((int(uv.x*texSize.x/t)+int(uv.y*texSize.y/t))%2)*0.8+0.1;
}

void main(){
    vec2 st = uv;
    vec3 color = vec3(0.0);

    // Use polar coordinates instead of cartesian
    vec2 toCenter = vec2(0.5)-st;
    float angle = atan(toCenter.y, toCenter.x) * 2.0;
    float radius = length(toCenter)*2.0;

    color = hsb2rgb(vec3((angle/TWO_PI)+0.5, radius, 1.0));
    color*=clamp(sin(st.x+st.y*10.0),0.,1.);
    color.x*=5.0;
    color.x=clamp(color.x,0.,1.);
    color=color*0.8+0.2;
    color*=checkerboard();

    fc = vec4(color, 1.0);
}
