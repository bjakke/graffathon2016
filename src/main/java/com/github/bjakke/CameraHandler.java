package com.github.bjakke;

import moonlander.library.Moonlander;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class CameraHandler {
	
	private final PApplet applet;
	//private final PeasyCam camera;
	private final PGraphics pg;
	private final Moonlander ml;

	//constructed on the setup method
	public CameraHandler(PApplet applet, PGraphics pg, Moonlander ml) {
		this.applet = applet;
		
		//this.pos = new PVector(0, 0, 300);
		//this.camera = new PeasyCam(applet, pg, 0,0,0,500);
		this.pg = pg;
		this.ml = ml;
	}
	
	//this is the processing draw() method call (called from the actual draw)
	public void draw(float timeSeconds){
		//peasyCamera();
		//followCamera(timeSeconds);		
		rocketCamera();
	}
	
	private void followCamera(float timeSeconds){
		PVector lap = helix(timeSeconds, new PVector(50, 50));
		PVector cam = helix(timeSeconds-10, new PVector(50, 50));
		pg.camera(cam.x, cam.y, cam.z, 
				lap.x, lap.y, lap.z, 
				0, 1, 0);
	}
	
	private void fpsCamera(){
//		applet.beginCamera();
//		applet.translate(pos.x, pos.y, pos.z);
//		
//		applet.endCamera();
	}
	
	private PVector helix(float time, PVector params){
		float a = params.x;
		float b = params.y;
		float x = a * applet.cos(time);
		float y = a * applet.sin(time);
		float z = b * time;
		return new PVector(x, y, z);
	}
	
	private float ml(String name){
		return (float) ml.getValue(name);
	}
	
	private void peasyCamera(){
		
		//camera.lookAt(0, 0, 300, 500);
	}
	
	private void rocketCamera(){	
		float eyeX = ml("eX");
		float eyeY = ml("eY");
		float eyeZ = ml("eZ");
		float centerX = ml("tX");
		float centerY = ml("tY");
		float centerZ = ml("tZ");
		pg.camera(eyeX, eyeY, eyeZ, 
				centerX, centerY, centerZ, 
				0, 1, 0);
		
		
		
	}

	
	
}
