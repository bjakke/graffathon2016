package com.github.bjakke;

import moonlander.library.Moonlander;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.opengl.PJOGL;

public class Main extends PApplet{
	private static final int[] SCENE_SIZE = new int[]{640, 480};
	//private static final int[] SCENE_SIZE = new int[]{320, 240};
	//private static final int[] SCENE_SIZE = new int[]{160, 120};
	
	private static final String FILE_NAME = "Furious Freak.mp3";
	private static final int BPM = 160;
	private static final int RPB = 8;
	//public static int numOfCubes = 1000;
	public static int screen = 0;
	
	public static void main(String[] args) {
		if(args != null && args.length > 0){
			screen = Integer.parseInt(args[0]);
		}
		PApplet.main("com.github.bjakke.Main");
	}
	private Moonlander ml;
	
	private ShaderTest st;
	private CameraHandler cameraHander;

	private PGraphics pg;

	private PostProcessShader pp;

	private PGraphics offboard;
	private long stop = 0;
	
	@Override
	public void draw() {
		System.out.println("FPS: "+frameRate);
		ml.update();
		
		//"scene", which state we are in
		int scene = ml.getIntValue("scene");
		
		if(stop == 0 && scene > 3){
			stop = millis();
		}
		
		//ending
		if(scene > 3){
			//end, show blank (+exit in finished ver)
			background(0);
			
			textSize(36);
			text("\"Paljon Toruja\"",100, 50);
			
			textSize(24);
			text("Code:", 100, 100);
			textSize(16);
			text("BJAKKE", 100, 150);
			
			textSize(24);
			text("Music:", 100, 250);
			textSize(16);
			text("\"Furious Freak\" Kevin MacLeod (incompetech.com)", 100,300);
			text("Licensed under Creative Commons: By Attribution 3.0 License", 100, 350);
			text("http://creativecommons.org/licenses/by/3.0/",100,400);
			
			
			if(millis() - stop > 5000){
				exit();
			}else{
				return;
			}
		}
		
		//int val = ml.getIntValue("my_track");
		pg.beginDraw();
		//rotate transformation
				
		int bgcolor = ml.getIntValue("bgcolor");
		
		pg.background(bgcolor); 
		//float seconds = millis() /1000.0f;
		float seconds = (float) ml.getCurrentTime();
		cameraHander.draw(seconds);
		st.draw(seconds);
		pg.endDraw();
		//draw simple floor
//		beginShape();
//		vertex(-500, -500, 0);
//		vertex(500, -500, 0);
//		vertex(500, 500,0);
//		vertex(-500, 500, 0);
//		endShape();
		
		//post processing, wont work
		//pp.draw(pg);
		
		//show offscreen image
		image(pg, 0, 0);
		
	}
	
	@Override
	public void keyPressed(KeyEvent event) {
		//if(event.getKeyCode() == )
	}

	@Override
	public void settings() {
		//size(SCENE_SIZE[0], SCENE_SIZE[1], P3D);
		fullScreen(P3D, screen);
		PJOGL.profile = 3;
	} 
	
	@Override
	public void setup() {
		//test offscreen drawing
		pg = createGraphics(width, height, P3D); //SCENE_SIZE[0], SCENE_SIZE[1], P3D);
		offboard = createGraphics(width, height, P3D); //SCENE_SIZE[0], SCENE_SIZE[1], P3D);
		
		//pg.background(0);		
		ml = Moonlander.initWithSoundtrack(this, 
				FILE_NAME, BPM, RPB);
		
		cameraHander = new CameraHandler(this, pg,ml);
		
		//camera = new PeasyCam(this, 0, 0, 0, 500);

	
		st = new ShaderTest(this, pg,
				"shaders/vert.glsl", "shaders/frag.glsl", ml);
		

		pp = new PostProcessShader(this,offboard);
		
		//background(0);
		
		ml.start("localhost", 1338, "syncdata.rocket");
	}

	
}
