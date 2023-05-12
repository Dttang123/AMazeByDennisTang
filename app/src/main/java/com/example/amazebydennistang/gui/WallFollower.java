package com.example.amazebydennistang.gui;

import com.example.amazebydennistang.generation.Floorplan;
import com.example.amazebydennistang.generation.Maze;
import com.example.amazebydennistang.gui.Robot.Direction;
import com.example.amazebydennistang.gui.Robot.Turn;

/**
 * Responsibilities:
 * WallFollower is an algorithm that implements the RobotDriver interface with the objective 
 * of quickly getting out of the maze by following the wall on its left-hand side until it reaches the exit.
 * 
 * The algorithm relies on sensors to detect if there is a wall in front of it and if there is one on the left side. 
 * If there is no wall to the left, it rotates left and moves forward in that direction.
 * Some issues related to the algorithm include recognizing the exit, handling sensor failure and repair (with a combination of two strategies as specified in the documentation), 
 * not allowing jumping over walls, and room handling.
 * 
 * If the robot is at an exit, the Robot.isAtExit method can be used to detect it and the robot can run for the exit and win since it can be seen from just one direction.
 * If the robot is in a hallway or in a room, it needs to be ensured that the driver goes to a wall and follows it, avoiding corners and circulating in the middle of the room.
 * If the forward/chosen side (left) sensor fails, the algorithm employs a sensor failure and repair strategy: substitute a failed sensor at runtime with a working one by rotating the robot to bring the working sensor into position, measuring the distance with the working sensor, and rotating the robot back to the original orientation. The second strategy involves waiting for the failed sensor to be repaired and then using it if no working sensor exists.
 * 
 * Collaborators: RobotDriver
 * 
 * @author Dennis Tang
 */

public class WallFollower implements RobotDriver {
	
	Robot robot;
	
	Maze ControlledMaze;
	
	public int energyUsed = 0;
	
	public int pathLength = 0;
	
	Direction direct;

	Floorplan flooplan;
	
	UnreliableRobot unreliableRobot;
	



	/*
	 * Assigns the initialized robot to r
	 */
	@Override
	public void setRobot(Robot r) {
		robot = r;
	}

	/*
	 * Assigns the initialized Maze to maze
	 */
	@Override
	public void setMaze(Maze maze) {
		ControlledMaze = maze;

	}

	/**
	 * Calls the "drive1Step2Exit" method repeatedly until the robot reaches the
	 * exit position.
	 * 
	 * Once the exit position is reached, the robot turns to face the exit.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		
		while (robot.isAtExit() == false) {
			drive1Step2Exit(); //Repeatedly called until exit is reached
			
		}
		if (robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
			robot.move(1);
			pathLength += 1;
			energyUsed += 7;
			
		} else if (robot.canSeeThroughTheExitIntoEternity(Direction.LEFT)) {
			robot.rotate(Turn.LEFT);
			robot.move(1);
			pathLength += 1;
			energyUsed += 11;
			
		} else if (robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT)) {
			robot.rotate(Turn.RIGHT);
			robot.move(1);
			pathLength += 1;
			energyUsed += 12;
			
		} else {
			robot.rotate(Turn.LEFT);
			robot.rotate(Turn.LEFT);
			robot.move(1);
			pathLength += 1;
			energyUsed += 15;
		}
		return true;
		
	}
	
	/**
	 * The robot follows a set of instructions based on its surroundings:
	 * 
	 * If the exit is visible to the left and there's enough power, turn left.
	 * 
	 * If there's no wall to the robot's left, turn left and move forward one step.
	 * 
	 * If there's a wall in front of the robot and to its left, turn right.
	 * 
	 * If there's no wall in front of the robot, move forward.
	 */

	@Override
	public boolean drive1Step2Exit() throws Exception {
		
		UnreliableSensor unreliableSensorLeft = new UnreliableSensor();
		unreliableSensorLeft.setSensorDirection(Direction.LEFT);
		
		UnreliableSensor unreliableSensorFront = new UnreliableSensor();
		unreliableSensorFront.setSensorDirection(Direction.FORWARD);
		

		if (robot.distanceToObstacle(Direction.LEFT) > 0) { //If there is no wall on the left side
			energyUsed += 1;
			if (robot.getBatteryLevel() >= 9) {
				robot.rotate(Turn.LEFT); //Rotates left
				robot.move(1); //Moves forward one space
				pathLength += 1; 
				energyUsed += 9; //Energy used for one rotation and step forward
				return true;
				
			} else {
				throw new Exception("Not enough power");
			}
		} 
		
		else if (robot.distanceToObstacle(Direction.LEFT) == 0 && robot.distanceToObstacle(Direction.FORWARD) == 0){ //If there is a wall to the left and in front of the robot
			energyUsed += 2;
			if (robot.getBatteryLevel() >= 3) {
				robot.rotate(Turn.RIGHT); //Rotates right 
				energyUsed += 3; //Energy used for one rotation 
				return true;
				
			} else {
				throw new Exception("Not enough power");
			}
			
		} else {
			energyUsed += 2;
			if (robot.getBatteryLevel() >= 6) {  
				robot.move(1); //Moves forward one space
				pathLength += 1;
				energyUsed += 6; //Energy used for one rotation
				return true;
				
			} else {
				throw new Exception("Not enough power");
			}
		}
	}

	/*
	 * Returns the energyUsed thus far
	 */
	@Override
	public float getEnergyConsumption() {
		return energyUsed;
	}
	
	/*
	 * Returns the PathLength of the algorithm
	 */
	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return pathLength;
	}

}