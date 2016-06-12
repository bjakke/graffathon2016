// vert.glsl
#version 330

#define M_PI 3.1415926535897932384626433832795

//Lookup table for cube coordinates
const vec3 LUT[36] = vec3[](
  //1
  vec3(-1.0,-1.0,-1.0),
  vec3(-1.0,-1.0, 1.0),
  vec3(-1.0, 1.0, 1.0),

  //2
  vec3( 1.0, 1.0,-1.0),
  vec3(-1.0,-1.0,-1.0),
  vec3(-1.0, 1.0,-1.0),

  //3
  vec3( 1.0,-1.0, 1.0),
  vec3(-1.0,-1.0,-1.0),
  vec3( 1.0,-1.0,-1.0),

  //4
  vec3( 1.0, 1.0,-1.0),
  vec3( 1.0,-1.0,-1.0),
  vec3(-1.0,-1.0,-1.0),

  //5
  vec3(-1.0,-1.0,-1.0),
  vec3(-1.0, 1.0, 1.0),
  vec3(-1.0, 1.0,-1.0),

  //6
  vec3( 1.0,-1.0, 1.0),
  vec3(-1.0,-1.0, 1.0),
  vec3(-1.0,-1.0,-1.0),

  //7
  vec3(-1.0, 1.0, 1.0),
  vec3(-1.0,-1.0, 1.0),
  vec3( 1.0,-1.0, 1.0),

  //8
  vec3( 1.0, 1.0, 1.0),
  vec3( 1.0,-1.0,-1.0),
  vec3( 1.0, 1.0,-1.0),

  //9
  vec3( 1.0,-1.0,-1.0),
  vec3( 1.0, 1.0, 1.0),
  vec3( 1.0,-1.0, 1.0),

  //10
  vec3( 1.0, 1.0, 1.0),
  vec3( 1.0, 1.0,-1.0),
  vec3(-1.0, 1.0,-1.0),

  //11
  vec3( 1.0, 1.0, 1.0),
  vec3(-1.0, 1.0,-1.0),
  vec3(-1.0, 1.0, 1.0),

  //12
  vec3( 1.0, 1.0, 1.0),
  vec3(-1.0, 1.0, 1.0),
  vec3( 1.0,-1.0, 1.0));



uniform mat4 transform;
uniform mat4 modelview;
uniform mat4 projection;


uniform float u_timeSeconds;

uniform float u_timeDisplacement;

uniform float u_cubeSize;

//torus
uniform int u_torusLoops;
uniform float u_torusMajor;
uniform float u_torusMinor;

uniform vec3 u_vrot;

//in vec4 position;
//in vec4 color;

out vec4 vertColor;
out int cube_id;
smooth out vec3 normal;
smooth out vec3 position;

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

vec3 calcNormal(int id){
  int idx = int(id - mod(id,3));
  vec3 vert1 = LUT[idx];
  vec3 vert2 = LUT[idx+1];
  vec3 vert3 = LUT[idx+2];
  //vec3 sum = vert1 + vert2 + vert3;
  //vec3 m = mod(abs(sum), 3); //now only one component is 1
  //return m * vert1; //give sign

  vec3 v1 = vert2-vert1;
  vec3 v2 = vert3-vert1;
  return normalize(cross(v1, v2));
}


vec3 helix(float t, vec2 params){
  float a = params.x;
  float b = params.y;
  float x = a * cos(t);
  float y = a * sin(t);
  float z = b * t;
  return vec3(x,y,z);
}

vec3 torus(float time, vec2 params){
  float r_minor = params.y;
  float r_major = params.x;

  float u = u_torusLoops * fract(time) * 2 * M_PI;//cos(2*M_PI*0.01*time) * 2 * M_PI;
  //float u = mod(0.01*time, 2*M_PI);

  float t = fract(time) * 2 * M_PI;

  float zt = fract(time*0.01) * 2 * M_PI;

  //float t = time;
  //float u = time*0.01;
  //t defined in params
  float x = cos(t) * (r_major + r_minor * cos(u));
  float y = sin(t) * (r_major + r_minor * cos(u));
  float z = r_minor * sin(u); // + zt;
  return vec3(x,y,z);
}

//gives time for individual cube
float cubeTime(int id){
  float t = u_timeSeconds;
  return t + u_timeDisplacement * id;
}

void calc(int id, out vec3 pos, out vec3 dir){
  float t = u_timeSeconds;
  float dt = 0.0001;

  //helix
  //pos = helix(t + id * 0.1, vec2(500,50));
  //vec3 posd = helix(t+dt + id * 0.1, vec2(500,50));
  //dir = normalize(posd - pos);

  //torus
  pos = torus(t + id * u_timeDisplacement, vec2(u_torusMajor,u_torusMinor));
  vec3 posd = torus(t+dt + id * u_timeDisplacement, vec2(u_torusMajor,u_torusMinor));
  dir = normalize(posd - pos);
}

void main() {

  //this is the number of cube
  int id = gl_VertexID /36;

  //this is the number of vertex (0-35)
  vec3 pos = LUT[int(mod(gl_VertexID, 36))];

  //size of cube
  pos *= u_cubeSize;

   //per instance transformations
  vec3 worldLoc;
  vec3 worldDir;
  calc(id, worldLoc, worldDir);

  //orientation
  //the cube normal orientation
  vec3 cube0 = vec3(1,0,0); //any primary axis
  //float angle = acos(dot(worldDir, cube0)); //angle between
  //float angle = 1.0;
  //vec3 rotAxis = normalize(cross(worldDir, cube0)); //find the rotation axis
  //mat4 rotM = rotationMatrix(rotAxis, angle); //rot matrix
  //gl_Position = transform *  vec4(pos, 1);
  //pos.xyz = (rotM * vec4(pos,1)).xyz;

  mat4 rotAdd = rotationMatrix(normalize(vec3(1,1,1)), u_timeSeconds*300);
  pos.xyz = (rotAdd * vec4(pos,1)).xyz;
  //position
  pos += worldLoc;

  //rotate the world
  mat4 rotX = rotationMatrix(normalize(vec3(1,0,0)), u_vrot.x);
  pos.xyz = (rotX * vec4(pos,1)).xyz;

  mat4 rotY = rotationMatrix(normalize(vec3(0,1,0)), u_vrot.y);
  pos.xyz = (rotY * vec4(pos,1)).xyz;

  mat4 rotZ = rotationMatrix(normalize(vec3(0,0,1)), u_vrot.z);
  pos.xyz = (rotZ * vec4(pos,1)).xyz;



  vec4 posFinal = vec4(pos,1);
  gl_Position = transform * posFinal;
  float r = sin(id)*0.5 + 0.5;
  vertColor = vec4(1.0,1.0,1.0,1.0);
  cube_id = id;
  normal = (rotAdd * vec4(calcNormal(int(mod(gl_VertexID, 36))),0)).xyz;
  position = posFinal.xyz;
}
