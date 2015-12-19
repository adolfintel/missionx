uniform float screenWidth;

// automatic
varying float amountX;

void main()
{
    amountX = clamp( gl_Vertex.x/screenWidth, 0.0, 1.0 );
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

    gl_TexCoord[0] = gl_MultiTexCoord0;
}
