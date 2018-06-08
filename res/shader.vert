#version 450
#extension GL_ARB_separate_shader_objects : enable
out gl_PerVertex {    vec4 gl_Position;};
layout(location = 0) in vec3 inPosition;
layout(location = 1) in vec2 inUV;
layout(location = 0) out vec3 fragColor;
layout(binding = 0) uniform Camera{    mat4 mvp;} camera;layout(binding = 1) uniform Camera2{   	vec3 vek;} camera2;

 void main() {
    gl_Position = camera.mvp * vec4(inPosition + camera2.vek, 1);
    fragColor = vec3(inUV, 0);
}
