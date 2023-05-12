package com.example.amazebydennistang.gui;

import java.util.concurrent.TimeUnit;

import com.example.amazebydennistang.generation.CardinalDirection;
import com.example.amazebydennistang.generation.Floorplan;
import com.example.amazebydennistang.generation.Maze;
import com.example.amazebydennistang.gui.Robot.Direction;

/**
 * 
 * Responsibilities:
 * 
 * The UnreliableSensor is a specialized subclass of ReliableSensor that includes a cycling failure and repair process, where it alternates between being operational and non-operational at regular intervals. 
 * This behavior is implemented using threads, with one background thread per sensor. 
 * The sensor operates for 4 seconds, fails for 2 seconds, and then repeats this cycle indefinitely.
 * The sensor is designed to start in an operational state and remain in that state after any failure and repair. In cases where there are multiple unreliable sensors, there is a delay of 1.3 seconds between starting each sensor's process.
 * 
 * Collaborators: Extends ReliableSensor, Implements DistanceSensor, Maze/Floorplan for management of distances and obstacles
 * 
 * @author Dennis Tang
 *
 */


class UnreliableSensor extends ReliableSensor implements DistanceSensor { //implements runnable?
	
	Direction direction;
	Maze controlledMaze;
	Floorplan floorplan;
	
	/*
	 * First, check if there is a wall in the given direction using the "hasWall()" function. 
	 * 
	 * If there is a wall, return 0 since the robot is already at the wall.
	 * 
	 * Next, create a variable called "moves" to keep track of the number of moves taken without hitting a wall.
	 * 
	 * Move the robot forward in the given direction until it encounters a wall, and for each step forward where there is no wall, increment the "moves" variable by 1.
	 * 
	 * Note that each step forward will decrease the "powerSupply" by 6, and each turn will decrease it by 3.
	 * 
	 * Finally, if a wall is encountered, return the "moves" variable as the distance to the nearest obstacle.
	 * 
	 *Checks 
	 */

	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		int moves = 0;
		
		if (powersupply[0] < 0) {
			throw new IndexOutOfBoundsException("Power supply is not in range");
		}
		
		if (currentPosition[0] < 0 || currentPosition[0] >= controlledMaze.getWidth()){
			
			throw new IllegalArgumentException("Current position out of range");
		}
		
		if (currentPosition[1] < 0 || currentPosition[1] >= controlledMaze.getHeight()) {
			
			throw new IllegalArgumentException("Current position out of range");
		}
		
		if (currentDirection == CardinalDirection.North){ //Checks if the currentDirection the robot is facing is North
			powersupply[0] -= 3;
			if (powersupply[0] <0) {
				throw new Exception("Not enough power for operation");
			}
			
			while (floorplan.hasWall(currentPosition[0], currentPosition[1] + moves, CardinalDirection.North) == false) { //While there isn't a wall in that direction at the current position after the moves 
				moves += 1;
				powersupply[0] -= 6; 
				if (powersupply[0] < 0) {
					throw new Exception("Not enough power for operation");
				}
				if (moves > (controlledMaze.getHeight() - currentPosition[0])) {
					return Integer.MAX_VALUE;
				}
			}
		}
		
		
		if (currentDirection == CardinalDirection.South){ //Checks if the currentDirection the robot is facing is South
			powersupply[0] -= 3;
			if (powersupply[0] <0) {
				throw new Exception("Not enough power for operation");
			}
			
			while (floorplan.hasWall(currentPosition[0], currentPosition[1] - moves, CardinalDirection.South) == false) { //While there isn't a wall in that direction at the current position after the moves 
				moves += 1;
				powersupply[0] -= 6;
				if (powersupply[0] < 0) {
					throw new Exception("Not enough power for operation");
				}
				if (moves > (currentPosition[0])) {
					return Integer.MAX_VALUE;
				}
			}
		}
		
		
		
		if (currentDirection == CardinalDirection.East){ //Checks if the currentDirection the robot is facing is East
			powersupply[0] -= 3;
			if (powersupply[0] <0) {
				throw new Exception("Not enough power for operation");
			}
			
			while (floorplan.hasWall(currentPosition[0]  - moves, currentPosition[1], CardinalDirection.East) == false) { //While there isn't a wall in that direction at the current position after the moves 
				moves += 1;
				powersupply[0] -= 6;
				if (powersupply[0] < 0) {
					throw new Exception("Not enough power for operation");
				}
				if (moves > (currentPosition[0])) {
					return Integer.MAX_VALUE;
				}
			}
		}
		
		
		
		if (currentDirection == CardinalDirection.West){ //Checks if the currentDirection the robot is facing is West
			powersupply[0] -= 3;
			if (powersupply[0] <0) {
				throw new Exception("Not enough power for operation");
			}
			
			while (floorplan.hasWall(currentPosition[0] + moves, currentPosition[1], CardinalDirection.West) == false) { //While there isn't a wall in that direction at the current position after the moves 
				moves += 1;
				powersupply[0] -= 6;
				if (powersupply[0] < 0) {
					throw new Exception("Not enough power for operation");
				}
				if (moves > (controlledMaze.getWidth() - currentPosition[0])) {
					return Integer.MAX_VALUE;
				}
			}
		}
		return moves;
	}
	
	/*
	 * Sets the maze equal to the maze in the argument
	 */

	@Override
	public void setMaze(Maze maze) {
		controlledMaze = maze;
		
	}
	
	
	/*
	 * Assigns the direction to the mountedDirection of the Sensor
	 */
	@Override
	public void setSensorDirection(Direction mountedDirection) {
		direction = mountedDirection;
		
	}

	/*
	 * Returns the energy required for sensing 
	 */
	@Override
	public float getEnergyConsumptionForSensing() {
		int sensingEnergy = 1;
		return sensingEnergy;
	}
	
	
	public boolean operational;
	Thread thread;
	
	//UnreliableSensor UnreliableSensor = new UnreliableSensor();
	//Thread thread1 = new Thread(UnreliableSensor);

	/**
	 * Creates a private thread variable and in the constructor
	 * 
	 * Thread.sleep(meanTimeBetweenFailures)
	 * 
	 * Thread.start()
	 * 
	 * Setting operational to true means that it is on
	 * 
	 * Thread.sleep(4000)
	 * 
	 * Setting operational to false means that it is off
	 * 
	 * Thread.sleep(2000)
	 * 
	 * Initiates the process of repairing the sensor by making it operational for a
	 * period of time equal to the mean time between failures.
	 * 
	 * After this period, the sensor will be made non-operational again for a
	 * duration equal to the mean time to repair.
	 */
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					operational = true;
					try {
						TimeUnit.SECONDS.sleep(meanTimeBetweenFailures);
						//thread.sleep(meanTimeBetweenFailures * 1000);
					}	catch (InterruptedException e) {
						
						return;
					}
				}
			}
		};
		
		thread.start();
	}

	/**
	 * Interrupts the thread to halt the ongoing repairing process.
	 * 
	 * Sets the "operational" variable to true, indicating that the sensor is
	 * now operational again.
	 */
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		try {
			thread.interrupt();
		} catch (UnsupportedOperationException e) {
			return;
		}
		operational = true; //set to false because the thread ends? 
	}

	
}

/*	
	@Override
	public void run() {
		while(true) { //Replace with while state = statePlaying?
			try {
				operational = true; 
				thread.sleep(4000); //Operational for 4 seconds
				
				operational = false;
				thread.sleep(2000); //not Operational for 2 seconds
				
			} catch (Exception e) {
				
			}
			
			
		}
		
		
	}
}
*/