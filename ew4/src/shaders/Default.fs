uniform sampler2D myTexture;
varying vec4 Color;
void main()
{
    FragColor = Color * texture2D(myTexture, gl_TexCoord[0]);
}
