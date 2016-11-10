// Name: Divya Jagadeesh
// USC loginid: djagadee
// CS 455 PA3
// Fall 2016

import java.util.LinkedList;
import java.util.Arrays;


/**
   Maze class

   Stores information about a maze and can find a path through the maze
   (if there is one).
   
   Assumptions about structure of the maze, as given in mazeData, startLoc, and endLoc
   (parameters to constructor), and the path:
     -- no outer walls given in mazeData -- search assumes there is a virtual 
        border around the maze (i.e., the maze path can't go outside of the maze
        boundaries)
     -- start location for a path is maze coordinate startLoc
     -- exit location is maze coordinate exitLoc
     -- mazeData input is a 2D array of booleans, where true means there is a wall
        at that location, and false means there isn't (see public FREE / WALL 
        constants below) 
     -- in mazeData the first index indicates the row. e.g., mazeData[row][col]
     -- only travel in 4 compass directions (no diagonal paths)
     -- can't travel through walls
 */

public class Maze {
   
   public static final boolean FREE = false;				// no wall
   public static final boolean WALL = true;					// wall
   public static final boolean VISITED = true;				// free space has been visited during path search
   public static final boolean NOT_VISITED = false;			// free space hasn't been visited during path search
   
   private MazeCoord startLoc;		// start location of finding path
   private MazeCoord endLoc;		// end location of finding path
   private boolean[][] mazeData;	// maze data (walls and free space)
   
   private boolean hasBeenSearched;				// true if the path has already been searched for, false if a search hasn't been attempted yet
   private LinkedList<MazeCoord> mazePath;		// path of maze
   private boolean[][] visitedSpots;			// visited spots while trying to search for path
   

   /**
      Constructs a maze.
      @param mazeData the maze to search.  See general Maze comments for what
      goes in this array.
      @param startLoc the location in maze to start the search (not necessarily on an edge)
      @param endLoc the "exit" location of the maze (not necessarily on an edge)
      PRE: 0 <= startLoc.getRow() < mazeData.length and 0 <= startLoc.getCol() < mazeData[0].length
         and 0 <= endLoc.getRow() < mazeData.length and 0 <= endLoc.getCol() < mazeData[0].length

    */
   public Maze(boolean[][] mazeData, MazeCoord startLoc, MazeCoord endLoc)
   {
	   this.startLoc = startLoc;	// start location 
	   this.endLoc = endLoc;		// end location	
	   this.mazeData = mazeData;	// true/false maze data
	   
	   hasBeenSearched = false;												// whether or not path has been searched for
	   mazePath = new LinkedList<MazeCoord>();								// path of maze
	   visitedSpots = new boolean[mazeData.length][mazeData[0].length];		// visited spots during search for path
   }

   
   /**
   Returns the number of rows in the maze
   @return number of rows
   */
   public int numRows() {
      return mazeData.length;
   }

   
   /**
   Returns the number of columns in the maze
   @return number of columns
   */   
   public int numCols() {
      return mazeData[0].length;
   } 
 
   
   /**
      Returns true iff there is a wall at this location
      @param loc the location in maze coordinates
      @return whether there is a wall here
      PRE: 0 <= loc.getRow() < numRows() and 0 <= loc.getCol() < numCols()
   */
   public boolean hasWallAt(MazeCoord loc) {
	   if(mazeData[loc.getRow()][loc.getCol()] == WALL){
		   return true;
	   }
      return false;   
   }
   

   /**
      Returns the entry location of this maze.
    */
   public MazeCoord getEntryLoc() {
      return startLoc;   
   }
   
   
   /**
   Returns the exit location of this maze.
   */
   public MazeCoord getExitLoc() {
      return endLoc;
   }

   
   /**
      Returns the path through the maze. First element is starting location, and
      last element is exit location.  If there was not path, or if this is called
      before search, returns empty list.

      @return the maze path
    */
   public LinkedList<MazeCoord> getPath() {
	   
	   // returns empty path if a path hasn't been searched for
	   if (hasBeenSearched == false){
		   return new LinkedList<MazeCoord>();
	   }

      return mazePath;   

   }


   /**
      Find a path through the maze if there is one.  Client can access the
      path found via getPath method.
      @return whether path was found.
    */
   public boolean search()  {  
	   
	   if(hasBeenSearched && mazePath.size() > 0){		// searched is true if path's been searched and the path > 0
		   return true;
	   }
	   else if (hasBeenSearched && mazePath.size() == 0){		// search is false if path's been searched and the path size is 0, in other words there is no path 
		   return false;
	   }
	   
	   // if the entry or exit locations have a wall, a path cannot be found
	   if (mazeData[startLoc.getRow()][startLoc.getCol()] == WALL || mazeData[endLoc.getRow()][endLoc.getCol()] == WALL){
		   return false;
	   }
	   
	   return helper(startLoc.getRow(), startLoc.getCol());		// returns path

   }
   
   /*
    * PRIVATE METHODS
    */
   
   /*
    * Helper method that finds the actual path through the maze, and stores the path in a LinkedList.
    * Finds the path with previous row and column input parameters.
    * If a path is not found, the LinkedList stores no path.
    * @return true if path was found, false if path was not found.
    */
   
   private boolean helper(int row, int col) {
	   
	   hasBeenSearched = true;		// search for path has been attempted
	   
	   // if there is a wall, you cannot find a path through that element
	   if (mazeData[row][col] == WALL){
		   return false;
	   }
	   // cannot visit a visited element. this may lead to us going through an infinite cycle in the maze
	   if (visitedSpots[row][col] == VISITED){
		   return false;
	   }
	   // returns true once it's found the end of the maze
	   if(row == endLoc.getRow() && col == endLoc.getCol()){
		   return true;
	   }
	   
	   visitedSpots[row][col] = VISITED;		// declares current spot as visited
	   
	   // checks below neighbor
	   if (row >= 0 && row < mazeData.length-1){
		   if (helper(row + 1, col)) {
			   mazePath.add(new MazeCoord(row + 1, col));
			   return true;
		   }
	   }
	   // checks above neighbor
	   if (row > 0 && row < mazeData.length) {
		   if (helper(row - 1, col)) {
			   mazePath.add(new MazeCoord(row - 1, col));
			   return true;
		   }
	   }
	   // checks right neighbor
	   if (col >= 0 && col < mazeData[1].length-1) {
		   if (helper(row, col + 1)) {
			   mazePath.add(new MazeCoord(row, col + 1));
			   return true;
		   }
	   }
	   // checks left neighbor
	   if(col > 0 && col < mazeData[1].length) {
		   if (helper(row, col - 1)) {
			   mazePath.add(new MazeCoord(row, col - 1));
			   return true;
		   }
	   }
	   
	   // if the next row,column pair does not fall under any of the if conditions, there is no path, hence returns empty path
	   mazePath = new LinkedList<MazeCoord>();
	   return false;
	   
   }
   


}