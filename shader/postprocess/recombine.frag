#version 410 core

in vec2 uv;
uniform vec2 texSize;

uniform sampler2D original, bloom;

out vec4 fc;

void main(){
    vec3 orig=texture(original,uv).xyz;
    vec3 bloo=texture(bloom,uv).xyz;
    vec3 res=orig*0.5+bloo*2.0;
    //TODO: gamma
    res=pow(res,vec3(1.0/1.8));
    fc=vec4(res,1.0);
}