uniform sampler2D myTexture;

void main()
{
    // ivec2 sizes = textureSize(myTexture,0);
    vec4 color;
    color.a = 1.0;
    color = texture2D( myTexture, gl_TexCoord[0].xy);
    color.rgb = (color.r + color.g + color.b) / 3.0;

    if (color.r<0.2 || color.r>0.8)
        color.r = 0.0;
    else
        color.r = 1.0;

    if (color.g<0.2 || color.g>0.8)
        color.g = 0.0;
    else
        color.g = 1.0;
    if (color.b<0.2 || color.b>0.8)
        color.b = 0.0;
    else
        color.b = 1.0;

    gl_FragColor = color;
}
