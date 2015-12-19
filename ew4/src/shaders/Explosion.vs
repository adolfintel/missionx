
varying vec2 vTexCoord;
uniform vec2 P;
uniform vec2 Q;
const vec2 R = (Q - P)/2;

void main(void)
{

   // Clean up inaccuaracies
   //vTexCoord = vec2(sign( (R-gl_Vertex)/R ));
   vTexCoord = vec2( sign(gl_Vertex) );

   //gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
   // gl_Position = vec4(vec2(vTexCoord),0,1 );
   gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}