package mpi1213.isag.model;

import mpi1213.isag.main.ImageContainer;
import processing.core.PImage;

public class Enemy {

	public static final int MIN_WIDTH = 50;
	public static final int MIN_HEIGHT = 50;
	public static final int MAX_WIDTH = 250;
	public static final int MAX_HEIGHT = 200;
	public static final int MIN_DELTA = 1;
	public static final int MAX_DELTA = 3;
	public static final int DESTROY_TIMESPAN = 500;

	private int xPos;
	private int yPos;

	private int width;
	private int height;

	private double deltaX;
	private double deltaY;

	private PImage image;
	
	private long destroyTime = 0;
	
	private EnemyState state = EnemyState.ALIVE;

	// temporary colors
	private float r = 100;
	private float g = 100;
	private float b = 100;

	public Enemy(int windowWidth, int windowHeight) {
		double deltaX, deltaY;
		deltaX = (Enemy.MIN_DELTA + Math.random() * (Enemy.MAX_DELTA - Enemy.MIN_DELTA + 1)) * getRandomDirection();
		deltaY = (Enemy.MIN_DELTA + Math.random() * (Enemy.MAX_DELTA - Enemy.MIN_DELTA + 1)) * getRandomDirection();

		init(Enemy.MIN_WIDTH + Math.random() * (Enemy.MAX_WIDTH - Enemy.MIN_WIDTH + 1),
				Enemy.MIN_WIDTH + Math.random() * (Enemy.MAX_WIDTH - Enemy.MIN_WIDTH + 1), deltaX, deltaY, 10 + (float) Math.random() * (255 - 10),
				(float) Math.random() * (255 - 10), (float) Math.random() * (255 - 10));
		this.xPos = (int)(Math.random() * (windowWidth - this.width/2));
		this.yPos = (int)(Math.random() * (windowHeight - this.height/2));		
	}

	private double getRandomDirection() {
		if (Math.random() < 0.5) {
			return -1;
		} else {
			return 1;
		}
	}

	private void init(double width, double height, double deltaX, double deltaY, float r, float g, float b) {
		this.width = (int) width;
		this.height = (int) height;
		this.deltaX = (int) deltaX;
		this.deltaY = (int) deltaY;
		this.r = r;
		this.g = g;
		this.b = b;
		try {
			this.image = (PImage) ImageContainer.getRandomAlienImage().clone();
			image.resize(this.width, 0);
			this.height = image.height;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void move(int windowWidth, int windowHeight) {
		xPos += deltaX;
		yPos += deltaY;

		if (xPos < -(width/2) || xPos > (windowWidth - width/2)) {
			xPos -= deltaX;
			deltaX = -deltaX;
		}
		if (yPos < -(height/2) || yPos > (windowHeight - height/2)) {
			yPos -= deltaY;
			deltaY = -deltaY;
		}
	}

	public boolean isHit(int xPos, int yPos) {
		return xPos >= this.xPos && xPos <= (this.xPos + width) && yPos >= this.yPos && yPos <= (this.yPos + height);
	}

	public float getX() {
		return xPos;
	}

	public float getY() {
		return yPos;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getR() {
		return r;
	}

	public float getG() {
		return g;
	}

	public float getB() {
		return b;
	}

	public PImage getImage() {
		return this.image;
	}
	
	public void setImage(PImage image) {
		this.image = image;
		this.image.resize(this.width, this.height);
	}
	
	public void destroy(){
		state = EnemyState.DESTROYED;
		deltaX = deltaY = 0;
		destroyTime = System.currentTimeMillis();
	}
	
	public void markToBeRemoved(){
		state = EnemyState.TO_BE_REMOVED;
	}
	
	public EnemyState getState(){
		return state;
	}

	public void update(int width, int height) {
		move(width, height);
		if(state == EnemyState.DESTROYED){
			if(System.currentTimeMillis() > (destroyTime + DESTROY_TIMESPAN)){
				state = EnemyState.TO_BE_REMOVED;
			}
		}
	}
}
