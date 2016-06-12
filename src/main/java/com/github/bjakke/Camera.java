package com.github.bjakke;

import processing.core.PVector;

public class Camera {
	
	//http://stackoverflow.com/questions/13862246/opengl-fps-camera-using-glulookat
	
	private float angleX = 0.0f;
	private float angleY = 0.0f;
	private PVector direction = new PVector(0,0,0);
	private PVector position = new PVector(0,0,0);

	public float getAngleX() {
		return angleX;
	}

	public float getAngleY() {
		return angleY;
	}

	public PVector getDirection() {
		return direction;
	}

	public PVector getPosition() {
		return position;
	}
	
	public void setPosition(PVector newPos, PVector newDir){
		this.direction = newDir;
		this.position = newPos;
	}
	
}
