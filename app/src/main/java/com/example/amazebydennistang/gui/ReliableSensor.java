package com.example.amazebydennistang.gui;

import com.example.amazebydennistang.generation.CardinalDirection;
import com.example.amazebydennistang.generation.Floorplan;
import com.example.amazebydennistang.generation.Maze;
import com.example.amazebydennistang.gui.Robot.Direction;

/**
 * 
 * ReliableSensor is a concrete implementation of the DistanceSensor interface. 
 * 
 * It calculates the distance from the robot's current position to the nearest wall in the direction it is mounted.
 * 
 * The distance is the number of steps the robot needs to take to reach the wall in the specified direction. 
 * 
 * ReliableSensor uses cardinal directions (north, south, east, and west) to determine the direction it is mounted 
 * 
 * and uses this information to query the floor plan to determine the distance to the nearest wall.
 * 
 * Four instances of ReliableSensor are required to cover all four directions. 
 * 
 * In addition to the distance sensors, ReliableSensor also includes a room sensor that detects whether the robot 
 * 
 * is inside a room and an exit sensor that detects whether the robot is standing on a cell that is right at the exit.
 * 
 * ReliableSensor is considered reliable and fully functional at all times, unlike the UnreliableSensor class.
 * 
 * Responsibilities:
 * - Provides info on how distance is computed
 * - Info on how far a is wall from a given position at a certain degree (direction)
 * - Hides how current direction and position of robot is translated to cardinal direction
 * to calculate distance from a wall using the FloorPlan
 * 
 * Collaborators: DistanceSensor, Maze, FloorPlan 

 * @author Dennis Tang
 */



public class ReliableSensor implements DistanceSensor {
	
	Direction SensorDirection;
	Maze ControlledMaze;
	Floorplan floorplan;

	/**
	 * Calculates the distance to an obstacle in the given direction.
	 * Returns 0 if the robot is already at the wall.
	 * 
	 * Uses a count variable to keep track of how many steps were taken.
	 * The robot moves forward in the given direction until it reaches a wall,
	 * incrementing the count for each step taken where no wall is detected.
	 * 
	 * With each step taken, the power supply is decreased by 6. Each turn
	 * decreases the power supply by 3.
	 * 
	 * Returns the count as the distance to the obstacle once a wall is detected.
	 */

	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		
		if (powersupply[0] <= 0) {
			throw new ArrayIndexOutOfBoundsException("Not enough power to perform action");
		}
		
		int count = 0;
		if (currentDirection == CardinalDirection.East) {
			
			if (powersupply[0] < 0) {
				throw new ArrayIndexOutOfBoundsException("Not enough power to perform action");
			}
			while (ControlledMaze.hasWall(currentPosition[0]+count, currentPosition[1], CardinalDirection.East) == false) {
				count++;
				if (count >= (ControlledMaze.getWidth()-currentPosition[0])) {
					return Integer.MAX_VALUE;
				}
				
				if (powersupply[0] < 0) {
					throw new ArrayIndexOutOfBoundsException("Not enough power to perform action");
				}
				if (count > ControlledMaze.getWidth()) {
					return Integer.MAX_VALUE;
				}
			}
			
		}
	
		else if (currentDirection == CardinalDirection.West) {

			if (powersupply[0] < 0) {
				throw new ArrayIndexOutOfBoundsException("Not enough power to perform action");
			}
			
			while (ControlledMaze.hasWall(currentPosition[0]-count, currentPosition[1], CardinalDirection.West) == false) {
				count++;
				if (count >= (currentPosition[0])) {
					return Integer.MAX_VALUE;
				}

				if (powersupply[0] < 0) {
					throw new ArrayIndexOutOfBoundsException("Not enough power to perform action");
				}
				if (count > (ControlledMaze.getWidth())) {
					return Integer.MAX_VALUE;
				}
			}
			
		}
		else if (currentDirection == CardinalDirection.North) {

			if (powersupply[0] < 0) {
				throw new ArrayIndexOutOfBoundsException("Not enough power to perform action");
			}
			while (ControlledMaze.hasWall(currentPosition[0], currentPosition[1]-count, CardinalDirection.North) == false) {
				count++;
				if (count >= (currentPosition[1])) {
					return Integer.MAX_VALUE;
				}

				if (powersupply[0] < 0) {
					throw new ArrayIndexOutOfBoundsException("Not enough power to perform action");
				}
				
			}
			
		}
		else if (currentDirection == CardinalDirection.South) {
			
			if (powersupply[0] < 0) {
				throw new Exception("Not enough power to perform action");
			}
			while (ControlledMaze.hasWall(currentPosition[0], currentPosition[1]+count, CardinalDirection.South) == false) {
				count++;
				if (count >= (ControlledMaze.getHeight()-currentPosition[1])) {
					return Integer.MAX_VALUE;
				}
				if (powersupply[0] < 0) {
					throw new ArrayIndexOutOfBoundsException("Not enough power to perform action");
				}
				if (count > ControlledMaze.getHeight()) {
					return Integer.MAX_VALUE;
				}
			}
		}
		return count;
	}

	/*
	 * Sets the initialized maze 
	 */
	@Override
	public void setMaze(Maze maze) {
		ControlledMaze = maze;
		

	}
	
	/**
	 * Provides the angle, the relative direction at which this 
	 * sensor is mounted on the robot.
	 * If the direction is left, then the sensor is pointing
	 * towards the left hand side of the robot at a 90 degree
	 * angle from the forward direction. 
	 * @param mountedDirection is the sensor's relative direction
	 * @throws IllegalArgumentException if parameter is null
	 */
	
	/*
	 * Sets this sensors in a specific direction on the robot relative to it's facing direction
	 */
	@Override
	public void setSensorDirection(Direction mountedDirection) {
		
		assert(mountedDirection!=null);
		
		SensorDirection = mountedDirection;
	}

	/*
	 * Returns the constant 1, for each scan
	 */
	@Override
	public float getEnergyConsumptionForSensing() {
		return 1;
	}

	
	//Complete for P4
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}
	
	//Complete for P4
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}
