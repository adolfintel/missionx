uniform vec3 startColor;
uniform vec3 endColor;

// automatic
uniform sampler2D myTexture;
varying float amountX;

void main()
{
    vec3 color = endColor * amountX + startColor * (1-amountX);
    FragColor = texture2D(myTexture, gl_TexCoord[0]) * vec4(color,1);
}
