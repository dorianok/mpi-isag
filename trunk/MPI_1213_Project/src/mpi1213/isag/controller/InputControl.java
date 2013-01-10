package mpi1213.isag.controller;

import mpi1213.isag.model.Model;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class InputControl {
	private static final int MAX_PLAYERS = 2;
	
	InputMode inputMode = InputMode.KINECT;
	SimpleOpenNI context;
	Model model;

	public InputControl(PApplet pApplet, Model model) {
		context = new SimpleOpenNI(pApplet);
		this.model = model;

		if (SimpleOpenNI.deviceCount() < 0) {
			inputMode = InputMode.MOUSE;
		} else {
			context.enableDepth();
			context.enableRGB();
			context.setMirror(true);
			context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
			context.addGesture("Click");
		}
	}

	public void update() {
		if (inputMode == InputMode.KINECT) {
			context.update();
			
			for(Integer key:model.getPlayers().keySet()){
				if (context.isTrackingSkeleton(key)) {
					PVector hand3d = new PVector();
					PVector hand2d = new PVector();
					context.getJointPositionSkeleton(key,SimpleOpenNI.SKEL_RIGHT_HAND, hand3d);
					context.convertRealWorldToProjective(hand3d, hand2d);
					model.getPlayers().get(key).setPosition(hand2d);
				}	
			}
			
			
		} else {
			
		}
	}
	
	public PImage getDepthImage(){
		if(inputMode == InputMode.KINECT){
			return (PImage) context.depthImage();
		} else {
			return null;
		}
	}

	public void newPlayer(int userId) {
		context.requestCalibrationSkeleton(userId, true);
		System.out.println("user: " + userId);
	}

	public void endCalibration(int id, boolean successfull) {
		if (successfull) {
			System.out.println("successful, id: " + id);
			context.startTrackingSkeleton(id);
			model.addPlayer(id);			
		}
	}

	public void removePlayer(int userId) {
		model.removePlayer(userId);
		context.stopTrackingSkeleton(userId);
		System.out.println("player " + userId + " lost.");
	}

}
