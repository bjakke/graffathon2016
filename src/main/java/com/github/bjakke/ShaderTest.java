package com.github.bjakke;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import moonlander.library.Moonlander;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.opengl.PJOGL;
import processing.opengl.PShader;

public class ShaderTest{
	
	private final PShader shader;
	private final PApplet applet;
	private final PGraphics pg;
	private Moonlander ml;
	
	public ShaderTest(PApplet p, PGraphics pg, String vertFilePath, String fragFilePath, Moonlander ml) {
		applet = p;
		shader = new PShader(p, vertFilePath, fragFilePath);
		this.pg = pg;
		this.ml = ml;
	}
	
	//call this only from the Processing draw() method
	//pg the grapchis to draw on
	public void draw(float timeSeconds){
		PJOGL pgl = (PJOGL) pg.beginPGL();
		GL3 gl = pgl.gl.getGL3();
		
		//int cubes = 10000;
		//cubes += (applet.millis() / 1000 )*20 +1;
		
		
		//playing..
		//cubes += applet.sin(timeSeconds*0.1f)*200f;
		
		
		int cubes = ml.getIntValue("cubes");
		
		//safety limit for cube number
		if(cubes > 1000){
			cubes = 1000;
		}
		if(cubes < 1){
			cubes = 1;
		}
		
		
		System.out.println("CUBES:"+cubes);
		
		float d = (1.0f / cubes )* 2.0f * applet.PI;
		
		//better d for smaller cube amounts
		if(cubes < 299){
			d = applet.PI*50;
		}
		
		
		float cube_size = ml("cube_size");
		float time_multiplier = ml("time_div"); //1000
		
		
		
		shader.set("u_timeSeconds", timeSeconds / time_multiplier);
		shader.set("u_timeDisplacement", d);
		shader.set("u_cubeSize", cube_size);
		
		
		float rotVX = ml("rotVX");
		float rotVY = ml("rotVY");
		float rotVZ = ml("rotVZ");
		shader.set("u_vrot", new PVector(rotVX, rotVY, rotVZ));
		
		
		//torus		
		int torus_loops = mli("torus_loops"); //50 was nice
		float torus_major = ml("torus_major"); //200
		float torus_minor = ml("torus_minor"); //50
		
		shader.set("u_torusLoops", torus_loops);
		shader.set("u_torusMajor", torus_major);
		shader.set("u_torusMinor", torus_minor);
		
		
		PVector lightDir = new PVector(0, 0, 1);
		shader.set("u_lightDirection", lightDir);
		PVector lightInt = new PVector(1,0,0);
		shader.set("u_lightIntensity", lightInt);
		
		float pl_t = timeSeconds*1.0f;
		float pl_x = torus_major*1.2f * applet.cos(pl_t);
		float pl_y = torus_major*1.2f * applet.sin(pl_t);
		float pl_z = 0;
		
		PVector pLDir = new PVector(pl_x,pl_y,pl_z);
		shader.set("u_pointLightLocation", pLDir);
		PVector pLInt = new PVector(0.0f, 0.3f, 0.3f);
		shader.set("u_pointLightIntensity", pLInt);
		//shader.set("u_numCubes", num);
		
		shader.bind();
		
		//draw X vertexies..
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, 36*cubes);
				
		shader.unbind();
		pg.endPGL();

	}
	
	private float ml(String trackName){
		return (float) ml.getValue(trackName);
	}
	
	private int mli(String trackName){
		return ml.getIntValue(trackName);
	}
	
}