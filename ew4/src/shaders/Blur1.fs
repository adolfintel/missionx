uniform sampler2D myTexture;

void main()
{
    // 1 2 1
    // 2 1 2 / 13
    // 1 2 1
    ivec2 sizes = textureSize(myTexture,0);
    float stepx = 1/sizes.x;
    float stepy = 1/sizes.y;

    vec4 color:

     color =
        1.0 * texture2D(myTexture, vec2(gl_TexCoord[0].x - stepx , gl_TexCoord[0].y - stepy ) ) +
        1.0 * texture2D(myTexture, vec2(gl_TexCoord[0].x         , gl_TexCoord[0].y - stepy)  ) +
        1.0 * texture2D(myTexture, vec2(gl_TexCoord[0].x + stepx , gl_TexCoord[0].y - stepy)  ) +
        1.0 * texture2D(myTexture, vec2(gl_TexCoord[0].x - stepx , gl_TexCoord[0].y )         ) +
        1.0 * texture2D(myTexture, vec2(gl_TexCoord[0].x         , gl_TexCoord[0].y )         ) +
        1.0 * texture2D(myTexture, vec2(gl_TexCoord[0].x + stepx , gl_TexCoord[0].y )         ) +
        1.0 * texture2D(myTexture, vec2(gl_TexCoord[0].x - stepx , gl_TexCoord[0].y + stepy ) ) +
        1.0 * texture2D(myTexture, vec2(gl_TexCoord[0].x         , gl_TexCoord[0].y + stepy)  ) +
        1.0 * texture2D(myTexture, vec2(gl_TexCoord[0].x + stepx , gl_TexCoord[0].y + stepy)  );

    gl_FragColor = color / 9.0;
}
