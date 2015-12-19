// blur (low-pass) 3x3 kernel

uniform sampler2D sampler0;

void main(void)
{

    
    gl_TexCoord[0].y = gl_TexCoord[0].y + (sin(gl_TexCoord[0].x*100)*0.0064 );
    vec4 color = texture2D( sampler0, gl_TexCoord[0].st );
    gl_FragColor = color ;

    /*vec4 sample[9];
    vec2 tc_offset[9];

    tc_offset[0] = vec2(-1,-1);
    tc_offset[1] = vec2(0,-1);
    tc_offset[2] = vec2(1,-1);

    tc_offset[3] = vec2(-1,0);
    tc_offset[4] = vec2(0,0);
    tc_offset[5] = vec2(1,0);

    tc_offset[6] = vec2(-1,1);
    tc_offset[7] = vec2(0,1);
    tc_offset[8] = vec2(1,1);


    for (int i = 0; i < 9; i++)
    {
        sample[i] = texture2D(sampler0,
                              gl_TexCoord[0].st + 1/textureSize(sampler0,0)*tc_offset[i] );
    }

//   1 2 1
//   2 1 2   / 13
//   1 2 1

    gl_FragColor = (sample[0] + (2.0*sample[1]) + sample[2] +
                    (2.0*sample[3]) + sample[4] + (2.0*sample[5]) +
                    sample[6] + (2.0*sample[7]) + sample[8]) / 13.0;*/
}
