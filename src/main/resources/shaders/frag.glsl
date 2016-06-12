// frag.glsl
#version 330

uniform mat4 transform;

uniform vec3 u_lightDirection;
uniform vec3 u_lightIntensity;

uniform vec3 u_pointLightLocation;
uniform vec3 u_pointLightIntensity;

in vec4 vertColor;
smooth in vec3 normal;
smooth in vec3 position;

out vec4 fragColor;


//http://www.neilmendoza.com/glsl-rotation-about-an-arbitrary-axis/
mat4 rotationMatrix(vec3 axis, float angle){
    axis = normalize(axis);
    float s = sin(angle);
    float c = cos(angle);
    float oc = 1.0 - c;

    return mat4(oc * axis.x * axis.x + c,           oc * axis.x * axis.y - axis.z * s,  oc * axis.z * axis.x + axis.y * s,  0.0,
                oc * axis.x * axis.y + axis.z * s,  oc * axis.y * axis.y + c,           oc * axis.y * axis.z - axis.x * s,  0.0,
                oc * axis.z * axis.x - axis.y * s,  oc * axis.y * axis.z + axis.x * s,  oc * axis.z * axis.z + c,           0.0,
                0.0,                                0.0,                                0.0,                                1.0);
}

void main() {
  vec3 ldir = u_lightDirection;
  float cosangle = max(0,dot(normal, ldir));
  vec3 l = cosangle * u_lightIntensity;

  vec3 pldir = normalize(u_pointLightLocation - position);
  float pcosangle = max(0, dot(normal, pldir  / pow(length(pldir), 2.0)));
  vec3 pl = pcosangle * u_pointLightIntensity;

  vec3 material = vertColor.xyz;

  vec3 color = material * (l+pl);
  //color * pl;


  fragColor = vec4(color, 1);
}
