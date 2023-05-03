package com.example.amazebydennistang.generation;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/* Using Borukva's Algorithm, every wallboard is assigned a unique weight 
 * that will be used to determine what wall will be deleted.
 * Walls that are considered a "border", i.e. load-baring maze walls/room walls, will be not be deleted
 * Borders are maintained in order keep the maze and rooms intact
 */

public class MazeBuilderBoruvka extends MazeBuilder implements Runnable {

		
		//The cell's edge weight and at a specific position and direction
		public final HashMap <ArrayList<Integer>, Integer > totalEdgeWeights = new HashMap<>();
		
		//The cell's value from specific position
		public HashMap <Integer, ArrayList<Integer>> cellValueToPosition = new HashMap<>();
		
		//The cell's positions from specific value
		public HashMap <ArrayList<Integer>, Integer > positionToCellValue = new HashMap<>();
		

		

		
	/* Contains the super(); constructor call to invoke the constructor of the superclass MazeBuilder
	*/
	public MazeBuilderBoruvka(){

		super();
		
	}
	
	
	
	
	/* This will return the edge weights of the given wallboard.
	 * When this is called twice with the same input, the same values should be returned
	 * First put all the inputed segments into an array list
	 * Then get the value of the direction and position value using the getValueDirection method
	 * Using the value and direction, r
	 * eturn the edge weight
	 */ 
	public int getEdgeWeight(int x, int y, CardinalDirection cd) {

		 ArrayList<Integer> temp_pos = new ArrayList<Integer>();
		 
		 //Adds the current positions of the wallboard to the Arraylist
		 temp_pos.add(x);
		 temp_pos.add(y);
		 
		 //Retrieves the Value from the position
		 int cellVal = positionToCellValue.get(temp_pos);
		 
		 //Retrieves the assigned value of the direction
		 int numDirection = getNumDirection(cd);
		 
		 //Value of the cell and the number of the corresponding direction are put into a list
		 ArrayList<Integer> temp = new ArrayList<Integer>();
		 temp.add(cellVal);
		 temp.add(numDirection);
		 
		 
		 //returns the weight of the edge that is connected to the position and direction given
		 return totalEdgeWeights.get(temp);
		
		
		
	}

	
	
	
	/* The generate pathways method will use Boruvka's Algorithm to create a spanning tree 
	 * by deleting internal wallboards from an existing maze. Rooms will be generated before
	 * Boruvka's is applied. Uses the getEdgeWeight() method if it needs to select an edge
	 * according to its associated weight or cost. 
	 * Initally set all the edge weights of the wallboards
	 * Create lists of both the segments of individual cells and wallboards that are able to be deleted
	 * 		Each cell is treated as its own container 
	 * Add the positions of a cell into both a list and hashmap with their keys and values
	 * Go through all the cells that is not the current cell and check if they have wallbboards that can be deleted
	 * Continue until there are no more cells in the list of segments 
	 */
	
	@Override
	protected void generatePathways() {
		
		

		//Uses setEdgeWeights method to get all wallboards available
		setEdgeWeights(totalEdgeWeights);
		
		//the final keyword is used to indicate that a variable, method, or 
		//class cannot be changed or overridden once it has been initialized or defined.
		//List stores the segments of a cell
		final ArrayList<ArrayList<Integer>> listOfSegments = new ArrayList<ArrayList<Integer>>();
		
		//List stores wallboards that are going to be deleted
		final ArrayList<Wallboard> listToRemove = new ArrayList<Wallboard>();
		
		
	
		int SetsValue = 1;
		
		//Goes thought he cells of the maze and adds their positions to a list and hashmap 
		
		for(int y = 0; y <height; y++) {
			for(int x = 0; x <width; x++) {
				
				//Previously mentioned list of positions
				ArrayList<Integer> positions = new ArrayList<Integer>(2);
				positions.add(x);
				positions.add(y);
				
				//Adds the value of the cell into a key and the position into a value
				cellValueToPosition.put(SetsValue, positions);
				
				//Vice Versa
				positionToCellValue.put(positions,  SetsValue);
				
				SetsValue++;		
				
			}
			
		}
		
		//Adds each value from a specific cell into the list of positions (origin being equal to 1)
		for (int currentNum = 1; currentNum<= (height * width); currentNum++) {
			
			//Checks the directions
			ArrayList<Integer> ValidNorth = new ArrayList<Integer>();
			ValidNorth.add(currentNum);
			ValidNorth.add(0);
			
			ArrayList<Integer> ValidEast = new ArrayList<Integer>();
			ValidEast.add(currentNum);
			ValidEast.add(1);
			
			ArrayList<Integer> ValidSouth = new ArrayList<Integer>();
			ValidSouth.add(currentNum);
			ValidSouth.add(2);
			
			ArrayList<Integer> ValidWest = new ArrayList<Integer>();
			ValidWest.add(currentNum);
			ValidWest.add(3);
			
			//Checks if a wallboard can be deleted by checking if the hashmap contains the direction check key
			if(totalEdgeWeights.containsKey(ValidNorth) || (totalEdgeWeights.containsKey(ValidEast)) || (totalEdgeWeights.containsKey(ValidSouth)) || (totalEdgeWeights.containsKey(ValidWest))){
				
				//Puts number of cell into its individual arraylist before adding to segments
				ArrayList<Integer> cellNum = new ArrayList<Integer>();
				
				cellNum.add(currentNum);
				listOfSegments.add(cellNum);
			}
			
			
		}
		
		//Chooses the lowest edge weight not in the current branch
		//Goes through the list of segments and adds the segment with the lowest weight that is not currently in the branch
		
		while(listOfSegments.size() > 1 ) {
			
			//Checks list one by one
			for(ArrayList<Integer> currentSegment : listOfSegments) {
				
				//Determines the smallest weight of a wallboard
				Wallboard currentWallboard = findLowestWeight(currentSegment, totalEdgeWeights);
				
				//Adds that to the list to be deleted
				listToRemove.add(currentWallboard);
				
			}
			
			//Moves through the list to be deleted
			for(Wallboard thisWallboard : listToRemove) {
				
				//Checks for valid wall at that position and direction
				if( floorplan.hasWall(thisWallboard.getX(), thisWallboard.getY(), thisWallboard.getDirection())) {
					
					floorplan.deleteWallboard(thisWallboard);
					
					mergeSegments(thisWallboard, listOfSegments);
					
				}
			}
			
			//Clears the list to be deleted
			listToRemove.clear();	
			
		}
		
	}
	
	
	 /* 
	 * Cardinal Direction getDirection (int x):
	 * Return the Cardinal Direction when given an x value
	 */ 
	
	private CardinalDirection getDirection(int x) {
		if (x == 0) {
			return CardinalDirection.North;
		}
		if (x == 1) {
			return CardinalDirection.East;
		}
		if (x == 2) {
			return CardinalDirection.South;
		}
		else {
			return CardinalDirection.West;
		}
	}
	
	
	
	
	
	 /* 
	 * 	
	 * int getValueDirection(CardinalDirection direct):
	 * Assigns certain values to each direction and returns that value
	 */ 
	
	private int getNumDirection(CardinalDirection direction) {
		if (direction == CardinalDirection.North){
			return 0;
		}
		if (direction == CardinalDirection.East){
			return 1;
		}
		if (direction == CardinalDirection.South){
			return 2;
		}
		else {
			return 3;
		}
		
	}
	
	
	
	
	
	
	/*
	 * Used to help with creation of lists in merge method
	 */
	private ArrayList <Integer> makeList(ArrayList<Integer> list){
		
		ArrayList<Integer> new_List = new ArrayList<Integer>();
		
		for (int x : list) {
			
			new_List.add(x);
		
		}
		
		return new_List;
	}
	

	
	
	
	
	
	/*
	 * Wallboard LowestWeight():
	 * Finds the wallboard with the lowest edge weight that isn't in the current segment or "load-baring" wall
	 * Takes in the edge weights and hashmaps containing segment information
	 * Uses these to find the lowest edge weight of all of them. 
	 */
	private Wallboard findLowestWeight(ArrayList<Integer> segment, HashMap <ArrayList<Integer>, Integer > totalEdgeWeights) {
		

		//
		Wallboard lowestWeightWallboard = new Wallboard(0,0,CardinalDirection.North);
		
		
		int minimumWeight = Integer.MAX_VALUE;
		
		//Go through the each segment's value
		for(int curSegmentValue : segment) {
			
			//Obtains the position from the value
			ArrayList<Integer> positions = cellValueToPosition.get(curSegmentValue);
			
			int x = positions.get(0);
			int y = positions.get(1);
			
			//Checks all CardinalDirections and adds the direction values to the list
			for( CardinalDirection direction : CardinalDirection.values()) {
				
				int numDirection = getNumDirection(direction);
				
				ArrayList<Integer> getWeight = new ArrayList<Integer>();
				
				getWeight.add(curSegmentValue);
				getWeight.add(numDirection);
				
				//Checks if there is a wall at the position and edgeweight
				if( floorplan.hasWall(x, y, direction) && (totalEdgeWeights.get(getWeight) != null)) {
					
					Wallboard tempWall = new Wallboard(x,y, direction);
					
					//Checks for border/load-baring walls, runs if there aren't any
					if( floorplan.isPartOfBorder(tempWall)) {
						
						int neighborValue = curSegmentValue;
						
						if(direction == CardinalDirection.North) {
							neighborValue -= width;
						}
						else if(direction == CardinalDirection.South) {
							neighborValue += width;
						}
						else if(direction == CardinalDirection.East) {
							neighborValue += 1;
						}
						else {
							neighborValue -= 1;
						}
						
						//Checks if the neighbor is a part of the segment and if the new edgeweight is less than the current mimimum
						if(!segment.contains(neighborValue) && (minimumWeight > getEdgeWeight(tempWall.getX(), tempWall.getY(), tempWall.getDirection()))) {
							
							lowestWeightWallboard = new Wallboard(x,y,direction);
							
							//Assigns the new minimum to the weight of the current edgeweight
							minimumWeight = getEdgeWeight(tempWall.getX(), tempWall.getY(), tempWall.getDirection());
							
							
					
						}
						
						
					}
					
				}	
				
			}
			
			
		}
		
		return lowestWeightWallboard;
	
	}
	
	
	
	
	
	
	 /* 
	 * void MergeSegments():
	 * Merges two lists containing the segments generated from the algorithm and makes them into one list
	 * Acts as the connecting portion of Boruvka's Algorithm, when the segments of the spanning tree connect 
	 * via minimum edge. 
	 */
	private void mergeSegments(Wallboard curWallboard, ArrayList<ArrayList<Integer>> listSegments) {
		
		//Creates lists that are used for merging 
		ArrayList<Integer> merge_List1 = new ArrayList<Integer>();
		ArrayList<Integer> merge_List2 = new ArrayList<Integer>();
		
		//Creates list for wallboard positions
		final ArrayList<Integer> curWallboardPositions = new ArrayList<Integer>();
		
		curWallboardPositions.add(curWallboard.getX());
		curWallboardPositions.add(curWallboard.getY());
		
		//Value at current position
		int curValue = positionToCellValue.get(curWallboardPositions);
		
		//Goes through each segment of the list and checks for the current value to add to the intial list
		for(ArrayList <Integer> arrayList : listSegments) {
			
			if(arrayList.indexOf(curValue) != -1) {
				
				merge_List1.addAll(makeList(arrayList));
				
				listSegments.remove(arrayList);
				break;
			}
		}
		
		//Matches the neighbor's values with the wallboard directional values
		if(curWallboard.getDirection() == CardinalDirection.North) {
			curValue =- width;
		}
		else if (curWallboard.getDirection() == CardinalDirection.South) {
			curValue =+ width;
			
		}
		else if (curWallboard.getDirection() == CardinalDirection.East) {
			curValue =+ 1;
		}
		else {
			curValue =- 1;
			
		}
		
		//Moves through list of segments and searches that segement for the neighbor's value
		for(ArrayList<Integer> neighborList : listSegments) {
			if(neighborList.indexOf(curValue) != -1) {
				
				merge_List2.addAll(makeList(neighborList));
				
				listSegments.remove(neighborList);
				break;
			}
		}
		
		//combines both lists
		merge_List1.addAll(merge_List2);
		
		//adds combined list back to the list of segments
		listSegments.add(merge_List1);
 		
	}
	
	
	
	
	//Sets wallboards with a random edge weight that is based on the neighbor cell
	private HashMap<ArrayList<Integer>, Integer> setEdgeWeights(HashMap<ArrayList<Integer>, Integer> totalEdgeWeights){
		
		//Current cell value
		int SetsValue = 0;
		
		
		//Sets all wallboards that are initially -1
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				SetsValue++;
				
				//Checks for "load-baring" walls
				for(CardinalDirection directions : CardinalDirection.values()) {
					if(floorplan.hasWall(x, y, directions)) {
						
						Wallboard wallboard = new Wallboard(x, y, directions);
						
						//Checks for "load-baring" walls
						//Creates arraylist of integers and adds the values of the directions
						if(!floorplan.isPartOfBorder(wallboard)) {
							
							ArrayList<Integer> valList = new ArrayList<Integer>();
							valList.add(SetsValue);
							valList.add(getNumDirection(directions));
							
							//Later adds that into the hashmap with with -1 as value
							totalEdgeWeights.put(valList, -1);
							
						}
					}
				}
			}
		}
		
		//Creates a new random variable
		Random r = new Random();
		
		
		//Initializes Initial Hashset
		HashSet<Integer> Hset = new HashSet<>();
		
		//Moves through all the wallboards and sets walls 
		for(ArrayList<Integer>list: totalEdgeWeights.keySet()) {
			
			//Finds that value inside the hashmap and finds if the weight is still -1
			if(totalEdgeWeights.get(list) == -1) {
				
				int random = r.nextInt(4 * height * width); //All sides of the square maze using the dimensions of the maze
				
				//Continues if there is a random number in the set
				while(Hset.contains(random)) {
					random = r.nextInt(4 * height * width); //All sides of the square maze using the dimensions of the maze
				}
			
				Hset.add(random);
				
				//Changes the default weight to the new weights
				totalEdgeWeights.replace(list, random);
				
				//Creates an opposite direction variable to later use when setting the edge weight of the opposite wall to the same random integer
				CardinalDirection curDirection = getDirection(list.get(1));
				CardinalDirection opposite = curDirection.oppositeDirection();
				
				//Value of the neighbor
				int curValue = 0;
				if (curDirection == CardinalDirection.North) {
					curValue = list.get(0) - width;
				}
				else if (curDirection == CardinalDirection.South) {
					curValue = list.get(0) + width;
					
				}
				else if (curDirection == CardinalDirection.East) {
					curValue = list.get(0) + 1;
				}
				else {
					curValue = list.get(0) - 1;
					
				}
				
				// Makes the opposite wallboard weight the random int
				ArrayList<Integer> oppositeWallBoard = new ArrayList<Integer>();
				
				oppositeWallBoard.add(curValue);
				oppositeWallBoard.add(getNumDirection(opposite));
				
				//Replaces opposite wallboard with the same random int 
				totalEdgeWeights.replace(oppositeWallBoard, random);
				
				
			}
		}
			
		//returns all edge weights in a hashmap
		return totalEdgeWeights;
		
		
	}
	
	
	
	

}

