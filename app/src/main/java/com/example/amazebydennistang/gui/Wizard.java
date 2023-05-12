package com.example.amazebydennistang.gui;


import com.example.amazebydennistang.generation.CardinalDirection;
import com.example.amazebydennistang.generation.Distance;
import com.example.amazebydennistang.generation.Maze;
import com.example.amazebydennistang.gui.Robot.Direction;
import com.example.amazebydennistang.gui.Robot.Turn;


/**
 * Uses the Wizard algorithm to drive the robot to the exit 
 * Or one step till the exit. By using the next NeighborCloserToExit,
 * Initialize both Maze and Robot that the drive will use to move through the maze.
 * 
 * Responsibilities: 
 * - Finds way out of Maze by using NeighborCloserToExit
 * - Fails if sensors drive algorithm into a wall
 * - Uses sensors to confirm wall location
 * - Uses Info from Maze via the setMaze method
 * - Good for testing maze, robot, sensor
 * - Baseline algorithm to test algorithm efficiency
 * 
 * Collaborators: ReliableRobot (RobotDriver), Maze
 * 
 * @author Dennis Tang
 *
 */

public class Wizard implements RobotDriver {
	
	Robot robot;
	
	Maze ControlledMaze;
	
	public int energyUsed = 0;
	
	public int pathLength = 0;

	private int[] currentPosition;
	
	//Direction direct;
	
	//StatePlaying play = new StatePlaying();
	


	/*
	 * Assigns the initialized robot to r
	 */
	@Override
	public void setRobot(Robot r) {
		this.robot = r;

	}

	/*
	 * Assigns the initialized Maze to maze
	 */
	@Override
	public void setMaze(Maze maze) {
		this.ControlledMaze = maze;
		this.currentPosition = maze.getStartingPosition();

	}
	/**
	 * Drives the robot towards the exit following
	 * its solution strategy and given the exit exists and  
	 * given the robot's energy supply lasts long enough. 
	 * When the robot reached the exit position and its forward
	 * direction points to the exit the search terminates and 
	 * the method returns true.
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception.
	 * If the method determines that it is not capable of finding the
	 * exit it returns false, for instance, if it determines it runs
	 * in a cycle and can't resolve this.
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	
	/*
	 * Create a variable to represent the cost of the battery. While the robot hasn't reached the final position, check if the current cell has already been visited. If it has, return false.
	 * 
	 * If the current cell hasn't been visited yet, determine which cell is closest to the exit and set the battery cost variable to the amount of battery power required to reach that cell.
	 * 
	 * If the battery cost is greater than the amount of battery power remaining, throw an exception. Otherwise, mark the current cell as visited and move to the next cell.
	 * 
	 * Once the robot reaches the final position, it should turn towards the exit and return true. If the robot is unable to make it to the exit, return false.
	 * 
	 * If the battery level drops below zero at any point during this process, throw an exception.

	 */
	@Override
	public boolean drive2Exit() throws Exception {
		while (robot.isAtExit() == false) {
			drive1Step2Exit();
		}
		
		if (robot.isAtExit()) {
			if (robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
				robot.move(1);
				pathLength += 1;
				energyUsed += 6;
			} 
			else if (robot.canSeeThroughTheExitIntoEternity(Direction.LEFT)) {
				robot.rotate(Turn.LEFT);
				robot.move(1);
				pathLength += 1;
				energyUsed += 9;

			} 
			else if (robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT) ){
				robot.rotate(Turn.RIGHT);
				robot.move(1);
				pathLength += 1;
				energyUsed += 9;

			}
			energyUsed += 3;
		}

		return false;

	}

	
	/**
	 * Drives the robot one step towards the exit following
	 * its solution strategy and given the exists and 
	 * given the robot's energy supply lasts long enough.
	 * It returns true if the driver successfully moved
	 * the robot from its current location to an adjacent
	 * location.
	 * At the exit position, it rotates the robot 
	 * such that if faces the exit in its forward direction
	 * and returns false. 
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception. 
	 * @return true if it moved the robot to an adjacent cell, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	
	/*
	 * If the current position of the robot is the same as the final position, turn the robot towards the exit and return false.
	 *
	 * Otherwise, determine which cell is closest to the exit and calculate the cost of getting to that cell.
	 *
	 * If the current battery level is less than the cost of the battery needed to get to that cell, or if there is a wall in the way, throw an exception.
	 *
	 * If there are no obstacles and the robot has enough battery, proceed to the selected cell and return true. 
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		
		CardinalDirection finalDirection = null;
		if (robot.isAtExit()) {
			
			if (robot.canSeeThroughTheExitIntoEternity(Direction.LEFT)) {
				robot.rotate(Turn.LEFT);
				energyUsed += 3;
			}
			else if (robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT)) {
				robot.rotate(Turn.RIGHT);
				energyUsed += 3;

			}
			energyUsed += 2;
			return false;
			
		} else {
			
			int[] cell = ControlledMaze.getNeighborCloserToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]);			

			if (cell[0] > robot.getCurrentPosition()[0]) {
				finalDirection = CardinalDirection.East;
			
			} 
			
			else if (cell[0] < robot.getCurrentPosition()[0]) {
				finalDirection = CardinalDirection.West;

			}
			
			
			if (cell[1] > robot.getCurrentPosition()[1]) {
				finalDirection = CardinalDirection.South;

			} 
			
			else if (cell[1] < robot.getCurrentPosition()[1]) {
				finalDirection = CardinalDirection.North;

			}
			
			while(robot.getCurrentDirection() != finalDirection) {
				if (robot.getBatteryLevel() >=3 ) {
					robot.rotate(Turn.LEFT);
					energyUsed += 3;
				} else {
					throw new Exception("Low battery");
				}
				
			}
			if (robot.getBatteryLevel() >= 6) {
				robot.move(1);
				pathLength += 1;
				energyUsed += 6;

			} else {
				
				throw new Exception("Low battery");
			}
			
			return true;

		}
	}

	/*
	 * returns the energy consumed variable
	 */
	@Override
	public float getEnergyConsumption() {
		return energyUsed;
	}

	/*
	 * returns the pathLength variable
	 */
	@Override
	public int getPathLength() {
		return pathLength;
	}

}
