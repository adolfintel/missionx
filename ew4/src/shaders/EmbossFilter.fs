#define KERNEL_SIZE 9
// Emboss kernel
// 2  0  0
// 0 -1  0
// 0  0  -1
const float kernel[KERNEL_SIZE] = { 2.0,   0.0,   0.0,
                                    0.0,  -1.0,   0.0,
                                    0.0,   0.0,  -1.0 };

uniform sampler2D colorMap;

const vec2 sizes = textureSize( colorMap, 0 );
const float step_w = 1.0/sizes.x;
const float step_h = 1.0/sizes.y;

const vec2 offset[KERNEL_SIZE] = {  vec2(-step_w, -step_h), vec2(0.0, -step_h), vec2(step_w, -step_h),
                                    vec2(-step_w, 0.0),     vec2(0.0, 0.0),     vec2(step_w, 0.0),
                                    vec2(-step_w, step_h),  vec2(0.0, step_h),  vec2(step_w, step_h) };


void main(void)
{
    vec4 sum = vec4(0.0);

    for( int i=0; i<KERNEL_SIZE; i++ )
    {
        vec4 tmp = texture2D(colorMap, gl_TexCoord[0].st + offset[i]);
	sum += tmp * kernel[i];
    }
    FragColor = sum + texture2D(colorMap, gl_TexCoord[0].st);
}