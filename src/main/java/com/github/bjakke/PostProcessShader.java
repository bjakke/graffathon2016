package com.github.bjakke;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PShader;

public class PostProcessShader {

	private final PApplet applet;
	private final PShader shader;
	private final PGraphics offboard;

	public PostProcessShader(PApplet applet, PGraphics offboard) {
		this.applet = applet;
		this.offboard = offboard;
		this.shader = applet.loadShader("shaders/postprocess.glsl");
	}
	
	public void draw(PGraphics thingToPostProcess){
		PGraphics pg = thingToPostProcess;
		
		applet.shader(shader);
		applet.image(thingToPostProcess, 0, 0);
//		
//		offboard.beginDraw();
//		PJOGL pgl = (PJOGL) offboard.beginPGL();
//		GL3 gl = pgl.gl.getGL3();
//		
//		shader.set("u_text", pg.get());
//		shader.set("u_resolution", (float)applet.width, (float)applet.height);
//		
//		shader.bind();
//		
//		applet.rect(0, 0, applet.width, applet.height);
//		
//		shader.unbind();
//		offboard.endPGL();
//		offboard.endDraw();
	}
	
}
