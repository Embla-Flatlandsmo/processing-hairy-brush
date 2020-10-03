import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.ArrayList; 
import java.io.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class build extends PApplet {


 


ArrayList<Particle> pts;
boolean onPressed;

public void setup() {
	
	
	frameRate(30);
	colorMode(HSB);
	rectMode(CENTER);

	pts = new ArrayList<Particle>();

	onPressed = false;

	background(0);
}

public void draw() {
	if (onPressed) {
		for (int i = 0; i<10; i++) {
		Particle p = new Particle(mouseX, mouseY,i,i);
		pts.add(p);
		}
	}

	Iterator<Particle> it = pts.iterator();
	while (it.hasNext()) {
		Particle p = it.next();
		p.update();
		p.display();
		if (p.dead) {
			it.remove();
		}
	}
}

public void mousePressed() {
	onPressed = true;
}


public void mouseReleased() {
	onPressed = false;
}

public void keyPressed() {
	if (key == 'c') {
		Iterator<Particle> it = pts.iterator();
		while (it.hasNext()) {
			it.remove();
		}
	}
}



class Particle{
	PVector location, velocity, acceleration;
	int lifeSpan, passedLife;
	float alpha, size, sizeRange, decay, xOffset, yOffset;
	boolean dead;
	int c;

	Particle(float x, float y, float xOffset, float yOffset) {
		location = new PVector(x,y);

		//Random intial velocity in random direction
		float randDegrees = random(360);
		velocity = new PVector(cos(radians(randDegrees)), sin(radians(randDegrees)));
		velocity.mult(random(5));

		//Initial conditions
		acceleration = new PVector(0,0);
		lifeSpan = PApplet.parseInt(random(30,90));
		passedLife = 0;
		decay = random(0.75f,0.9f);
		sizeRange = random(3,50);
		dead = false;

		colorMode(HSB,360,100,100);
		c = color(random(0,60), random(40,90), random(0,20));

		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void update() {
		if (passedLife>= lifeSpan) {
			dead = true;
		} else {
			passedLife++;
		}

		alpha = PApplet.parseFloat(lifeSpan-passedLife)/lifeSpan * 70 + 50;
		size = PApplet.parseFloat(lifeSpan-passedLife)/lifeSpan * sizeRange;

		acceleration.mult(0);

		float randomRadian = noise((location.x+frameCount+xOffset)*0.01f, (location.y+frameCount+yOffset)*0.01f)*4*PI;
		float randomMagnitude = noise((location.x+xOffset)*0.01f, (location.y+yOffset)*0.01f);

		PVector dir = new PVector(cos(randomRadian), sin(randomRadian));
		acceleration.add(dir);
		acceleration.mult(randomMagnitude);

		velocity.add(acceleration);
		velocity.mult(decay);
		velocity.limit(3);

		location.add(velocity);
		float hue = hue(c);
		float saturation = saturation(c);
		float brightness = brightness(c);
		if (brightness <= 80) {
			brightness += 2;
		}

		c = color(hue,saturation,brightness);

	}

	public void display() {
		strokeWeight(size+1.5f);
		stroke(0,alpha);
		point(location.x,location.y);

		strokeWeight(size);
		stroke(c);
		point(location.x,location.y);
	}
}
  public void settings() { 	size(720,720,P2D); 	smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "build" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
