uniform float speed;
uniform bool finalSum;
uniform sampler2D colorMap;

const vec2 sizes = textureSize( colorMap, 0 );
const float stepX = 1.0/sizes.x;
const float stepY = 1.0/sizes.y;

void main(void)
{
    vec4 sum = vec4(0.0);

    int count = 0;
    bool found = false;
    for( int i=0; i<speed; i++ )
    {
        vec4 tmp = texture2D(colorMap, gl_TexCoord[0].st + vec2(float(i)*stepX,0) );
        if (tmp.r+tmp.g+tmp.b+tmp.a > 0) {
            found = true;
            sum += tmp * (float(speed-i)/speed);
            ++count;
        } else {
            if (found) break;
        }
    }
    sum /= count;
    if (finalSum) {
        sum += texture2D(colorMap, gl_TexCoord[0].st)/2;
    }
    gl_FragColor = sum;
}