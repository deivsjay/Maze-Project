// Name: Divya Jagadeesh
// USC loginid: djagadee
// CS 455 PA3
// Fall 2016

import java.awt.Graphics;
import javax.swing.JComponent;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Iterator;

/**
   MazeComponent class
   
   A component that displays the maze and path through it if one has been found.
*/
public class MazeComponent extends JComponent
{
   private Maze maze;
   
   private static final int START_X = 10; // where to start drawing maze in frame
   private static final int START_Y = 10;
   private static final int BOX_WIDTH = 20;  // width and height of one maze unit
   private static final int BOX_HEIGHT = 20;
   private static final int INSET = 2;  
                    // how much smaller on each side to make entry/exit inner box
   
   
   /**
      Constructs the component.
      @param maze   the maze to display
   */
   public MazeComponent(Maze maze) 
   {   
      this.maze = maze;
   }

   
   /**
     Draws the current state of maze including the path through it if one has
     been found.
     @param g the graphics context
   */
   public void paintComponent(Graphics g)
   {
	   
	   // maze frame
	   Graphics2D g2 = (Graphics2D) g;
	   Rectangle drawMaze = new Rectangle(START_X, START_Y, maze.numCols()*BOX_WIDTH, maze.numRows()*BOX_HEIGHT);	
	   g2.draw(drawMaze);
	   
	   // draws black walls in maze
	   for (int i = 0; i < maze.numRows(); i++){
		   for (int j = 0; j < maze.numCols(); j++){
			   if(maze.hasWallAt(new MazeCoord(i,j))){
				   Rectangle wall = new Rectangle(START_X + j * BOX_WIDTH, START_Y + i * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT); 
				   g2.setColor(Color.black);
				   g2.fill(wall);
			   }
		   }
	   }
	   
	   // draws yellow box for entry location
	   if(!maze.hasWallAt(maze.getEntryLoc())){
		   g2.setColor(Color.yellow);
		   Rectangle start = new Rectangle(START_X + maze.getEntryLoc().getCol() * BOX_WIDTH + INSET, START_Y + maze.getEntryLoc().getRow() * BOX_HEIGHT + INSET, BOX_WIDTH - 2 * INSET, BOX_HEIGHT - 2 * INSET);
		   g2.fill(start);
	   }
	   
	   // draws green box for exit location
	   if(!maze.hasWallAt(maze.getExitLoc())){
		   g2.setColor(Color.green);
		   Rectangle start = new Rectangle(START_X + maze.getExitLoc().getCol() * BOX_WIDTH + INSET, START_Y + maze.getExitLoc().getRow() * BOX_HEIGHT + INSET, BOX_WIDTH - 2 * INSET, BOX_HEIGHT - 2 * INSET);
		   g2.fill(start);
	   }
	   
	   // if there is a path, draw it
	   if (maze.getPath().size() > 0){
		   drawPath(g);		// calls method to draw path
	   }

   }
   
   /*
    * PRIVATE METHODS
    */
   
   /*
    * Draws maze path.
    * @param g the graphics context
    */
   
   private void drawPath(Graphics g){
	   
	   Graphics2D g2 = (Graphics2D) g;
	   
	   LinkedList<MazeCoord> path = new LinkedList<MazeCoord>(maze.getPath());		// path of maze
	   int sizeOfPath = path.size();												// size of path
	   Iterator<MazeCoord> iter = path.iterator();									// iterate through path
	   MazeCoord startIter = iter.next();											// first element in path
	   MazeCoord currentIter = new MazeCoord(0,0);									// current element in path declared to 0,0
	   g2.setColor(Color.blue);														// path color is blue
	   
	   // goes through while loop as long as there is a next element in the path or the size of the path is greater than 0
	   while(iter.hasNext() || sizeOfPath > 0){
		   
		   // makes currentIter equal to the next element in the path
		   if (iter.hasNext()){
			   currentIter = iter.next();
		   }
		   sizeOfPath--;		// decrements size of path
		   
		   // draws path line if next element in path is below previous element in path
		   if(currentIter.getRow() == startIter.getRow() + 1){
			   g2.drawLine(START_X + startIter.getCol() * BOX_WIDTH + (BOX_WIDTH / 2), START_Y + startIter.getRow() * BOX_HEIGHT + (BOX_HEIGHT / 2), START_X + startIter.getCol() * BOX_WIDTH + (BOX_WIDTH / 2), START_Y + startIter.getRow() * BOX_HEIGHT + BOX_HEIGHT + (BOX_HEIGHT / 2));
		   }
		   // draws path line if next element in path is above previous element in path
		   else if(currentIter.getRow() == startIter.getRow() - 1){
			   g2.drawLine(START_X + startIter.getCol() * BOX_WIDTH + (BOX_WIDTH / 2), START_Y + startIter.getRow() * BOX_HEIGHT + (BOX_HEIGHT / 2), START_X + startIter.getCol() * BOX_WIDTH + (BOX_WIDTH / 2), START_Y + startIter.getRow() * BOX_HEIGHT - (BOX_HEIGHT/2));
		   }
		   // draws path line if next element in path is to the right of previous element in path
		   else if(currentIter.getCol() == startIter.getCol() + 1){
			   g2.drawLine(START_X + startIter.getCol() * BOX_WIDTH + (BOX_WIDTH / 2), START_Y + startIter.getRow() * BOX_HEIGHT + (BOX_HEIGHT / 2), START_X + startIter.getCol() * BOX_WIDTH + BOX_WIDTH + (BOX_WIDTH / 2), START_Y + startIter.getRow() * BOX_HEIGHT + (BOX_HEIGHT / 2));
		   }
		   // draws path line if next element in path is to the left of previous element in path
		   else if(currentIter.getCol() == startIter.getCol() - 1){
			   g2.drawLine(START_X + startIter.getCol() * BOX_WIDTH + (BOX_WIDTH / 2), START_Y + startIter.getRow() * BOX_HEIGHT + (BOX_HEIGHT / 2), START_X + startIter.getCol() * BOX_WIDTH - (BOX_WIDTH / 2), START_Y + startIter.getRow() * BOX_HEIGHT + (BOX_HEIGHT / 2));
		   }
		   
		   // if the size of the path is 1, we must draw last line between start location and first element in path
		   if (sizeOfPath == 1){
			   startIter = currentIter;
			   currentIter = maze.getEntryLoc();
		   }
		   // otherwise update start iterator as current iterator and keep proceeding through linked list until path is drawn
		   else {
			   startIter = currentIter;
		   }
		} 
   }
   
}


