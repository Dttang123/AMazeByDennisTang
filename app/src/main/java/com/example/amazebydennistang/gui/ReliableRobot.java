package com.example.amazebydennistang.gui;

import android.service.controls.Control;

import com.example.amazebydennistang.generation.Floorplan;
import com.example.amazebydennistang.generation.Maze;
import com.example.amazebydennistang.generation.CardinalDirection;


/*

 * The ReliableRobot class implements the Robot interface and is designed to function with reliable sensors.
 * It uses the information gathered from its sensors to make decisions based on the Wizard strategy, 
 * and carries out these decisions using its actuator methods.
 * To execute these actuator operations (such as movement or rotation), the ReliableRobot class interacts with the game controller Control.
 *
 *
 * Responsibilities: 
 * - Uses controller to Move and Rotate
 * - Interacts with the 4 directional sensors of Controller
 * - Gets info on current position and facing direction on Maze from StatePlaying
 * 
 * Collaborators: Robot, Controller, StatePlaying (From Controller), and controller
 * 
 * @author Dennis Tang
 */

public class ReliableRobot implements Robot {
	
	
	private static final float SENSOR_COST = 1;
	private static final float TURN_COST = 3;
	private static final float MOVE_FORWARD_COST = 6;
	private static final float JUMP_COST = 40;

	Maze controlledMaze;
	//Control control = new Control();
	PlayingActivityOrganizer activityOrganizer = new PlayingActivityOrganizer();
	float[] batteryLevel = new float [1];
	int odometer;
	Direction[] direct = {null, null, null, null};
	boolean hasStopped;
	protected Floorplan floorplan;

	private int[] currentPosition;
	
	ReliableSensor sensor = new ReliableSensor();
	
	//ROBOT HAS DIFFERENT REFERENCES TO 4 SENSOR CLASSES!!!
	DistanceSensor reliableSensorLeft = new ReliableSensor();
	DistanceSensor reliableSensorRight = new ReliableSensor();
	DistanceSensor reliableSensorFront = new ReliableSensor();
	DistanceSensor reliableSensorBack = new ReliableSensor();


	@Override
	/*
	 * Sets the initialized Control 
	 */
	public void setOrganizer(PlayingActivityOrganizer playingActivityOrganizer) {

		this.activityOrganizer = playingActivityOrganizer;
	}


	
	int numberOfSensors = 0;
	
	/*
	 * Create a variable to keep track of the number of sensors. 
	 * 
	 * Assign one of the four sensor instances created to the mountedDirection provided. 
	 * 
	 * Increase the count of sensors by 1.
	 */
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		
			if (mountedDirection == Direction.LEFT) {
				
				reliableSensorLeft = sensor;
				reliableSensorLeft.setSensorDirection(mountedDirection);
				direct[numberOfSensors] = mountedDirection;
				
				numberOfSensors++;
				
			} else if (mountedDirection == Direction.RIGHT) {
				
				reliableSensorRight = sensor;
				reliableSensorRight.setSensorDirection(mountedDirection);
				direct[numberOfSensors] = mountedDirection;
				
				numberOfSensors++;
				
			} else if (mountedDirection == Direction.FORWARD){
				
				reliableSensorFront = sensor;
				reliableSensorFront.setSensorDirection(mountedDirection);
				direct[numberOfSensors] = mountedDirection;
				
				numberOfSensors++;
				
			} else {
				
				reliableSensorBack = sensor;
				reliableSensorBack.setSensorDirection(mountedDirection);
				direct[numberOfSensors] = mountedDirection;
				
				numberOfSensors++;
				
			}


	}
	
	/*
	 * Initializes a maze and finds it's position using .getCurrentPosition()
	 * Returns that position if it is valid
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {
		try {
			activityOrganizer.getCurrentPosition();
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Position is not in the maze");
		}
		return activityOrganizer.getCurrentPosition();
	}


	/*
	 * Returns the current direction using getCurrentDirection
	 */
	@Override
	public CardinalDirection getCurrentDirection() {

		return activityOrganizer.getCurrentDirection();
	}

	/*
	 * Returns the initialized batteryLevel
	 */
	@Override
	public float getBatteryLevel() {
		return batteryLevel[0];
	}

	/*
	 * Sets the initialized batteryLevel()
	 */
	@Override
	public void setBatteryLevel(float level) {
		batteryLevel[0]= level;
	}

	/*
	 * Return the initialized cost of a turn
	 */
	@Override
	public float getEnergyForFullRotation() {
		return TURN_COST * 4;
	}

	/*
	 * Return the initialized cost of a moving forward 1 space
	 */
	@Override
	public float getEnergyForStepForward() {
		return MOVE_FORWARD_COST;
	}
	
	
	/*
	 * Return int using initialized odometer variable
	 */
	@Override
	public int getOdometerReading() {
		// TODO Auto-generated method stub
		return odometer;
	}

	/*
	 * Sets the initialized odometer to 0
	 */
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
		
		if (getBatteryLevel() >= 3) {
			if (turn == Turn.LEFT) {
				activityOrganizer.userInput(Constants.UserInput.LEFT);
				batteryLevel[0] -= 3;
			} else if (turn == Turn.RIGHT) {
				activityOrganizer.userInput(Constants.UserInput.RIGHT);
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
		if (distance < 0) {
			throw new IllegalArgumentException("Negative distance");
		}
		int count = 0;
		while (count < distance) {
			if (getBatteryLevel() >= 6) {
				activityOrganizer.userInput(Constants.UserInput.UP);
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
		if (hasStopped() == false) {
			move(1);
		}
		else if (controlledMaze.isValidPosition(currentPosition[0], currentPosition[1] - 1)) {
			activityOrganizer.userInput(Constants.UserInput.UP);
			odometer += 1;
			batteryLevel[0] -= 40;
		}
	}

	/*
	 * Uses isExitPosition from floorplan to getCurrentPosition
	 */
	@Override
	public boolean isAtExit() {
		controlledMaze = activityOrganizer.getMaze();
		try {
			if (controlledMaze.getExitPosition()[0] == getCurrentPosition()[0] && controlledMaze.getExitPosition()[1] == getCurrentPosition()[1]) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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

	/*
	 * Throw an UnsupportedOperationException if no sensor exists.
	 * 
	 * For each current direction, assign the turns based on the direction.
	 * 
	 * Afterwards, use the distanceToObstacle method with the current position and sensor direction.
	 * 
	 * If the result is invalid, an exception is thrown.
	 * 
	 * Otherwise, the method returns the distance available.
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		if (direction == Direction.LEFT) {
			
			CardinalDirection leftDirection;
			if (getCurrentDirection() == CardinalDirection.North) {
				leftDirection = CardinalDirection.East;
				
			} else if (getCurrentDirection() == CardinalDirection.East) {
				leftDirection = CardinalDirection.South;
				
			} else if (getCurrentDirection() == CardinalDirection.West) {
				leftDirection = CardinalDirection.North;
				
			} else {
				leftDirection = CardinalDirection.West;
			}
			try {
				return reliableSensorLeft.distanceToObstacle(getCurrentPosition(), leftDirection, batteryLevel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
		
		else if (direction == Direction.RIGHT) {
			
			CardinalDirection rightDirection;
			if (getCurrentDirection() == CardinalDirection.North) {
				rightDirection = CardinalDirection.West;
				
			} else if (getCurrentDirection() == CardinalDirection.East) {
				rightDirection = CardinalDirection.North;
				
			} else if (getCurrentDirection() == CardinalDirection.West) {
				rightDirection = CardinalDirection.South;
				
			} else {
				rightDirection = CardinalDirection.East;
			}
			try {
				return reliableSensorRight.distanceToObstacle(getCurrentPosition(), rightDirection, batteryLevel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
		
		else if (direction == Direction.FORWARD) {
			try {
				return reliableSensorFront.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), batteryLevel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
		else {
			CardinalDirection backDirection;
			if (getCurrentDirection() == CardinalDirection.North) {
				backDirection = CardinalDirection.South;
			} else if (getCurrentDirection() == CardinalDirection.East) {
				backDirection = CardinalDirection.West;
			} else if (getCurrentDirection() == CardinalDirection.West) {
				backDirection = CardinalDirection.East;
			} else {
				backDirection = CardinalDirection.North;
			}
			try {
				return reliableSensorBack.distanceToObstacle(getCurrentPosition(), backDirection, batteryLevel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		
		boolean exists = false;
		for (int i = 0; i < direct.length; i++) {
			
			if (direct[i] == direction) {
				exists = true;
			}
		}
		
		if (exists == false) {
			throw new UnsupportedOperationException("No sensor exists in this direction");
		}
		if (distanceToObstacle(direction) == Integer.MAX_VALUE) {
			return true;
		}
		else {
			return false;
		}

	}

	/*
	 * Finish in P4
	 */
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}
	
	/*
	 * Finish in P4
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}
