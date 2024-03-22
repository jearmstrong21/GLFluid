#version 410 core

uniform sampler2D velocity;

in vec2 uv;
uniform vec2 texSize;

out vec4 fc;

vec3 hsb2rgb(in vec3 c){
    vec3 rgb = clamp(abs(mod(c.x*6.0+vec3(0.0, 4.0, 2.0),
    6.0)-3.0)-1.0,
    0.0,
    1.0);
    rgb = rgb*rgb*(3.0-2.0*rgb);
    return c.z * mix(vec3(1.0), rgb, c.y);
}

void main(){
    vec2 v=texture(velocity,uv).xy;
    float angle=atan(v.y/v.x);
    float mag=length(v);

    fc=vec4(v*0.1+.5,0,1);
}