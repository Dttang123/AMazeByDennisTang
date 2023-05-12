package com.example.amazebydennistang.gui;

import com.example.amazebydennistang.generation.CardinalDirection;
import com.example.amazebydennistang.generation.Floorplan;
import com.example.amazebydennistang.generation.Maze;
import com.example.amazebydennistang.gui.Constants.UserInput;
import com.example.amazebydennistang.gui.Robot.Direction;
import com.example.amazebydennistang.gui.Robot.Turn;

public class UnreliableRobot implements Robot {
	
	private static final float SENSOR_COST = 1;
	private static final float TURN_COST = 3;
	private static final float MOVE_FORWARD_COST = 6;
	private static final float JUMP_COST = 40;

	Maze ControlledMaze;
	PlayingActivityOrganizer activityOrganizer = new PlayingActivityOrganizer();
	private PlayAnimationActivity animationActivity  = null;
	float[] batteryLevel = new float[1];
	int odometer;
	Direction[] direct = {null, null, null, null};
	boolean hasStopped;
	protected Floorplan floorplan;
	
	//StatePlaying play = new StatePlaying();
	
	//ROBOT HAS DIFFERENT REFERENCES TO 4 SENSOR CLASSES!!!
	UnreliableSensor unreliableSensorLeft = new UnreliableSensor();
	UnreliableSensor unreliableSensorRight  = new UnreliableSensor();
	UnreliableSensor unreliableSensorFront = new UnreliableSensor();
	UnreliableSensor unreliableSensorBack = new UnreliableSensor();

	/*
	 * Sets the initialized Control 
	 */
	@Override
	public void setOrganizer(PlayingActivityOrganizer playingActivityOrganizer) {
		this.activityOrganizer = playingActivityOrganizer;
	}

	/*
	 * Create a variable to keep track of the number of sensors. 
	 * 
	 * Assign one of the four sensor instances created to the mountedDirection provided. 
	 * 
	 * Increase the count of sensors by 1.
	 */
	public int numberOfSensors = 0;
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		
		if (mountedDirection == Direction.LEFT) {
			
			unreliableSensorLeft.setSensorDirection(mountedDirection);
			direct[numberOfSensors] = mountedDirection;
			
			numberOfSensors++;
			
		} else if (mountedDirection == Direction.RIGHT) {
			
			unreliableSensorRight.setSensorDirection(mountedDirection);
			direct[numberOfSensors] = mountedDirection;
			
			numberOfSensors++;
			
		} else if (mountedDirection == Direction.FORWARD){
			
			unreliableSensorFront.setSensorDirection(mountedDirection);
			direct[numberOfSensors] = mountedDirection;
			
			numberOfSensors++;
			
		} else {
			
			unreliableSensorBack.setSensorDirection(mountedDirection);
			direct[numberOfSensors] = mountedDirection;
			
			numberOfSensors++;
			
		}
	}

	@Override
	public int[] getCurrentPosition() throws Exception {
		try {
			activityOrganizer.getCurrentPosition();
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("This position is not in the Maze");
		}
		return activityOrganizer.getCurrentPosition();
	}

	@Override
	public CardinalDirection getCurrentDirection() {
		return activityOrganizer.getCurrentDirection();
	}

	@Override
	public float getBatteryLevel() {
		return batteryLevel[0];
	}

	@Override
	public void setBatteryLevel(float level) {
		batteryLevel[0] = level;

	}

	@Override
	public float getEnergyForFullRotation() {
		return TURN_COST * 4;
	}

	@Override
	public float getEnergyForStepForward() {
		return MOVE_FORWARD_COST;
	}

	@Override
	public int getOdometerReading() {
		return odometer;
	}

	@Override
	public void resetOdometer() {
		odometer = 0;

	}

	/*
	 * If the robot has not stopped:
	 *
	 * If the direction of the turn is left, decrease the battery level by 3 units when the user input is 'h'.
	 * If the direction of the turn is right, decrease the battery level by 3 units when the user input is 'l'.
	 */
	@Override
	public void rotate(Turn turn) {
		
		//control.setState(play); //Sets the state to StatePlaying to receive commands from robot
		activityOrganizer.setAnimationActivity(animationActivity);
		
		if (getBatteryLevel() >= 3) {
			
			if (turn == Turn.LEFT) {
				activityOrganizer.userInput(UserInput.LEFT);
				batteryLevel[0] -= 3;
				
			} else if (turn == Turn.RIGHT) {
				activityOrganizer.userInput(UserInput.RIGHT);
				batteryLevel[0] -= 3;
			}
		}


	}

	/*
	 * Throw an IllegalArgumentException if the distance parameter is less than 0.
	 * 
	 * Create a variable called "count".
	 * 
	 * While the value of "count" is less than the value of the "distance" parameter:
	 * 
	 * If the robot has not stopped, move the robot forward the given number of steps by entering 'k' as user input.
	 * 
	 * Increase the odometer reading by 1 for each step taken.
	 * 
	 * For each step taken, use the getEnergyForStepForward() method to determine the energy cost and subtract that value from the batteryLevel.
	 * 
	 * Increase the value of "count" by 1 for each step taken.
	 */
	@Override
	public void move(int distance) {
		//control.setState(play); //Sets the state to StatePlaying to receive commands from robot
		activityOrganizer.setAnimationActivity(animationActivity);
		if (distance < 0) {
			throw new IllegalArgumentException("Negative distance");
		}
		int count = 0;
		while (count < distance) {
			if (getBatteryLevel() >= 6) {
				activityOrganizer.userInput(UserInput.UP);
				odometer++;
				batteryLevel[0] -= getEnergyForStepForward();
				count++;
			}
		}
	}

	/*
	 * If the robot has stopped, enter 'k' as user input to move forward.
	 * 
	 * If the position in front of the robot has a wall and the battery level is greater than 40:
	 * 
	 * Enter 'k' as user input to move forward.
	 * 
	 * Increase the odometer reading by 1 for each step taken.
	 * 
	 * Subtract 40 from the battery level for each step taken.
	 */
	@Override
	public void jump() {
		//control.setState(play); //Sets the state to StatePlaying to receive commands from robot
		
		if (getBatteryLevel() >= 40 ) {
			activityOrganizer.userInput(UserInput.JUMP);
			odometer += 1;
			batteryLevel[0] -= 40;
		}
	}

	/*
	 * Uses isExitPosition from floorplan to getCurrentPosition
	 */
	@Override
	public boolean isAtExit() {
		ControlledMaze = activityOrganizer.getMaze();
		try {
			
			if (ControlledMaze.getExitPosition()[0] == getCurrentPosition()[0] && ControlledMaze.getExitPosition()[1] == getCurrentPosition()[1]) {
				
				return true;
			} else {
				
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
	}

	/*
	 * Checks the current position of the Maze and checks if that position in inside a room
	 */
	@Override
	public boolean isInsideRoom() {
		try {
			
			return floorplan.isInRoom(getCurrentPosition()[0], getCurrentPosition()[1]);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * If the distance to the obstacle in front of the robot is 0, return true.
	 * If the robot's battery level is less than 3, return true.
	 * Otherwise, return false.
	 * If the sensor is not working, throw an exception.
	 */
	@Override
	public boolean hasStopped() {
		try {
			
			if (distanceToObstacle(Direction.FORWARD) == 0) {
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (getBatteryLevel() < 3) {
			return true;
		}
		return false;
	}


	/**
	 * Throws an UnsupportedOperationException if no sensor exists.
	 * 
	 * If the sensor in the direction of the given parameter is operational, it senses the distance to the obstacle.
	 * 
	 * If the sensor is operational, the robot rotates left and recursively runs the method.
	 * 
	 * After the robot rotates, it rotates back to its original orientation to move appropriately.
	*/
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		
		boolean validSensor = false; //Initially starts out as false 
		
		for (int i = 0; i <= direct.length; i++) { //Checks for valid sensor in the direction provided in method parameter
			if (direct[i] == direction) {
				validSensor = true;
			}
		}
		if (validSensor == false) {
			throw new UnsupportedOperationException("There is not a sensor in this direction");
		}
		
		if (direction == Direction.LEFT) {
			if (unreliableSensorLeft.operational == true) {
				
				try {
					unreliableSensorLeft.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), batteryLevel);
					batteryLevel[0] -= 1;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			else {
				
				rotate(Turn.LEFT);
				distanceToObstacle(Direction.FORWARD);
				rotate(Turn.RIGHT);
				
			}
		}
		
		else if (direction == Direction.RIGHT) {
			if (unreliableSensorRight.operational == true) {
				
				try {
					unreliableSensorRight.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), batteryLevel);
					batteryLevel[0] -= 1;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			else {
				
				rotate(Turn.LEFT);
				distanceToObstacle(Direction.BACKWARD);
				rotate(Turn.RIGHT);
				
			}
		}
		
		else if (direction == Direction.FORWARD) {
			if (unreliableSensorFront.operational == true) {
				
				try {
					unreliableSensorFront.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), batteryLevel);
					batteryLevel[0] -= 1;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			else {
				
				rotate(Turn.LEFT);
				distanceToObstacle(Direction.RIGHT);
				rotate(Turn.RIGHT);
				
			}
		}
		
		else {
			if (unreliableSensorBack.operational == true) {
				
				try {
					unreliableSensorBack.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), batteryLevel);
					batteryLevel[0] -= 1;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			else {
				
				rotate(Turn.LEFT);
				distanceToObstacle(Direction.LEFT);
				rotate(Turn.RIGHT);
				
			}
		}
		
		return 0;
	}


	/*
	 * The method returns true if you can see through the exit in the specified direction.
	 * It is based on the idea that if the distance to the obstacle in the direction is MAXVALUE, then you can see through the exit.
	 */
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		
		boolean doesExists = false;
		for (int i = 0; i < direct.length; i++) {
			
			if (direct[i] == direction) {
				doesExists = true;
			}
		}
		
		if (doesExists == false) {
			throw new UnsupportedOperationException("There is not a sensor in this direction");
		}
		if (distanceToObstacle(direction) == Integer.MAX_VALUE) {
			return true;
		}
		else {
			return false;
		}

	}
	/*
	 * Begins the Failure/Repair Process for correct sensor given the argument directions
	 * 
	 */
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		
		if (direction == Direction.LEFT) {
			unreliableSensorLeft.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
		if (direction == Direction.RIGHT) {
			unreliableSensorRight.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
		if (direction == Direction.FORWARD) {
			unreliableSensorFront.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
		if (direction == Direction.BACKWARD) {
			unreliableSensorBack.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
		
	} 
	
	/*
	 * Stops the Failure/Repair Process for correct sensor given the argument directions
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		if (direction == Direction.LEFT) {
			unreliableSensorLeft.stopFailureAndRepairProcess();
		}
		if (direction == Direction.RIGHT) {
			unreliableSensorRight.stopFailureAndRepairProcess();
		}
		if (direction == Direction.FORWARD) {
			unreliableSensorFront.stopFailureAndRepairProcess();
		}
		if (direction == Direction.BACKWARD) {
			unreliableSensorBack.stopFailureAndRepairProcess();
		}
		
	}

}
