// variables settable by Pd
uniform float horizontal;
uniform float vertical;

// some helper variables
float xpos;
float ypos;
float maxhv;
float sum;

// texture comes from Pd
uniform sampler2D image;


void main()
{
        vec2 pos = gl_TexCoord[0].st;
        
        vec2 sizes = textureSize(image, 0);
        float w = sizes.x;
        float h = sizes.y;

	vec4 color = texture2D(image, pos);
	sum = 1.;
	maxhv = max(horizontal, vertical);

	for (float i=0.; i<=horizontal*2.; i++) {
		xpos = i-horizontal;
		for (float j=0.; j<=vertical*2.; j++) {
			ypos = j-vertical;
			color += texture2D(image,  (pos+vec2( xpos, ypos)) * ((maxhv+1.)-sqrt(xpos*xpos+ypos*ypos)) );
			sum+=(maxhv+1.)-sqrt(xpos*xpos+ypos*ypos);
		}
	}
	gl_FragColor = color/sum;
}