uniform float flamability;
uniform float pressure;
uniform float powerBoost;
uniform float intensity;
uniform float speed;
uniform float noisiness;
uniform float time_0_X;
uniform float explositivity;


//uniform sampler3D Noise;
uniform sampler2D Noise;
uniform sampler2D Flame;

varying vec2 vTexCoord;
float saturate(float inValue)
{
   return clamp(inValue, 0.0, 1.0);
}

void main(void)
{
  float t = fract(time_0_X * speed);

   // Alter the timing
   t = pow(t, explositivity);

   // The function f(t) = 6.75 * t * (t * (t - 2) + 1)
   // is a basic third degree pulse function with these properties:
   // f(0)  = 0
   // f(1)  = 0
   // f'(1) = 0
   // max(f(t)) = 1, where 0 < t < 1
   //
   // The basic idea of this function is a quick rise at the
   // beginning and then a slow smooth decline towards zero
   float size = intensity * 6.75 * t * (t * (t - 2.0) + 1.0);

   float dist = length(vTexCoord) / (0.1 + size);

   // Higher flamability => quicker move away from center
   // Higher pressure => tighter packing
   // float n = texture2D(Noise, vec3(noisiness * vTexCoord, flamability * time_0_X - pressure * dist)).x;
   // float n = vec3(noisiness * vTexCoord, flamability * time_0_X - pressure * dist).x;
   vec4 flame = texture2D(Flame, vec2(size * powerBoost + size * (2.0 /** n*/  - dist), 0.0));

   gl_FragColor = flame;

}