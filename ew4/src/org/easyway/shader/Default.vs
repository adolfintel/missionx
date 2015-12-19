attribute vec4 gl_Color;
varying vec4 gl_FrontColor;

uniform float angle;
uniform vec2  center;
void main()
{
    if (angle!=0) {
        vec2 pos   = vec2(gl_Vertex) - center;
        float cosA = cos( angle = radians( angle ) );
        float sinA = sin( angle );
        mat2 rot = mat2( cosA, sinA, -sinA, cosA );
        gl_Position  = gl_ModelViewProjectionMatrix * vec4(rot * pos + center,0,1);
    } else {
        gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    }
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_FrontColor = gl_Color;
}
