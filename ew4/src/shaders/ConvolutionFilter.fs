#define KERNEL_SIZE 9
uniform vec3 kernel0;
uniform vec3 kernel1;
uniform vec3 kernel2;

const float kernel[KERNEL_SIZE] = { kernel0.x,kernel0.y,kernel0.z,
                                    kernel1.x,kernel1.y,kernel1.z,
                                    kernel2.x,kernel2.y,kernel2.z };


uniform sampler2D colorMap;
uniform bool finalSum;

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
    if (finalSum) {
        sum += texture2D(colorMap, gl_TexCoord[0].st);
    }
    gl_FragColor = sum;
}