uniform sampler2D myTexture;

void main()
{
    ivec2 sizes = textureSize(myTexture,0);
    vec4 color;
    color.a = 1;
    color.rgb = 0.5;
    color -= texture2D( myTexture, gl_TexCoord[0].xy-(1.0/sizes.x))*2.0;
    color += texture2D( myTexture, gl_TexCoord[0].xy+(1.0/sizes.y))*2.0;
    gl_FragColor = (color.r+color.g+color.b)/3.0;
}
