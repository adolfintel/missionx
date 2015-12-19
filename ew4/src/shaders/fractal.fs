uniform vec2  Pan;
uniform float Zoom;
uniform float Aspect;

void main() {
    vec2 c = (gl_TexCoord[0] - 0.5) * Zoom * float2(1, Aspect) - Pan;
    vec2 v = 0;
        for (int n = 0; n < 128; n++) {
            v = float2(v.x * v.x - v.y * v.y, v.x * v.y * 2) + c;
        }
    gl_FragColor = dot(v,v);//(dot(v, v) > 1) ? 1 : 0;
}