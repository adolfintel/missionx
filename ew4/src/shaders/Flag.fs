uniform sampler2D sampler0;

uniform float lenght;
uniform float frequency;
uniform float time;

void main(void)
{
    gl_TexCoord[0].y = gl_TexCoord[0].y + (sin(gl_TexCoord[0].x*lenght+time)*frequency );
    vec4 color = texture2D( sampler0, gl_TexCoord[0].st );
    gl_FragColor = color ;
}