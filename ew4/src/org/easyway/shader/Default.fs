uniform sampler2D myTexture;

void main() 
{
    gl_FragColor = gl_Color * texture2D(myTexture, gl_TexCoord[0]);
}
