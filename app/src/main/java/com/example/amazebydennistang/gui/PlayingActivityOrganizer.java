package com.example.amazebydennistang.gui;

import android.graphics.Color;
import android.util.Log;

import com.example.amazebydennistang.generation.CardinalDirection;
import com.example.amazebydennistang.generation.Floorplan;
import com.example.amazebydennistang.generation.Maze;

public class PlayingActivityOrganizer {
    public enum ReasonForSwitch{WIN, CRASH, NOENERGY};
    /**
     * The first person view determines what is seen on the screen with a first person perspective.
     * This includes the background of two rectangles that cover the whole area.
     * Drawing polygons for walls is the main contribution.
     */
    private FirstPersonView firstPersonView;
    private Map mapView;
    CardinalDirection cd;
    private MazePanel panel;
    private int shortestPossibleSteps;
    private boolean manual;
    Floorplan seenCells;
    private boolean mapMode;
    private boolean showMaze;           // toggle switch to show overall maze on screen
    private boolean showSolution;
    private boolean started;
    //used to switch states once traversal has finished
    private PlayManuallyActivity manuallyActivity  = null;

    Maze maze;
    int px, py ; // current position on maze grid (x,y)
    int dx, dy ; // current position on maze grid (x,y)


    public void setManualActivity(PlayManuallyActivity pma){
        this.manuallyActivity = pma;
    }
    public void start(MazePanel panel, boolean manual) {
        // keep the reference to the panel for drawing
        this.panel = panel;
        this.manual = manual;

        //init all other variables
        initVariables();

        if (panel != null) {
            //FOR TESTING
            //drawBlueTriangle(panel);
            startDrawer();
        }
        else {
            // else: dry-run without graphics, most likely for testing purposes
            printWarning();
        }

    }
    private void initVariables(){
        this.started = true;
        this.setMaze(DataHolder.getMaze());
        // adjust internal state of maze model
        // visibility settings
        showMaze = false ;
        showSolution = false ;
        mapMode = false;
        // init data structure for visible walls
        seenCells = new Floorplan(maze.getWidth()+1,maze.getHeight()+1) ;
        cd = CardinalDirection.East;
        px = 0;
        py = 0;

        // set the current position and direction consistently with the viewing direction
        setPositionDirectionViewingDirection();

        this.setShortestDistance();


    }
    /**
     * Internal method to set the current position, the direction
     * and the viewing direction to values consistent with the
     * given maze.
     */
    private void setPositionDirectionViewingDirection() {
        int[] start = maze.getStartingPosition() ;
        setCurrentPosition(start[0],start[1]) ;
        cd = CardinalDirection.East;
    }



    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    protected void startDrawer() {


        firstPersonView = new FirstPersonView(Constants.VIEW_WIDTH,
                Constants.VIEW_HEIGHT, Constants.MAP_UNIT,
                Constants.STEP_SIZE, seenCells, maze.getRootnode()) ;


        mapView = new Map(seenCells, 15, maze) ;
        // draw the initial screen for this state
        draw(cd.angle(), 0);


    }

    public boolean userInput(Constants.UserInput userInput) {
        switch (userInput) {
            case UP: // move forward
                walk(1);
                //Checks if you left the maze
                if(!maze.isValidPosition(px,py)) {
                    if (manual) {
                        manuallyActivity.openWinning(manuallyActivity.getPathLength(), shortestPossibleSteps);
                    }
                }
                break;
            case LEFT: // turn left
                rotate(1);
                break;
            case RIGHT: // turn right
                rotate(-1);
                break;
            case JUMP:
                int[]tempDxDy = cd.getDxDyDirection();
                if (maze.isValidPosition(px + tempDxDy[0], py + tempDxDy[1])){
                    setCurrentPosition(px + tempDxDy[0],py + tempDxDy[1]);
                    draw(cd.angle(),0);
                }
                //If outside the maze then go to losing screen
                else {
                    if (manual){
                        manuallyActivity.openLosing(manuallyActivity.getPathLength(), shortestPossibleSteps);
                    }
                }
                break;
            case SHOWWALLS:
                mapMode = !mapMode;
                draw(cd.angle(),0);
                break;
            case SHOWFULLMAZE:
                showMaze = !showMaze;
                draw(cd.angle(), 0 );
                break;
            case SHOWSOLUTION:
                showSolution = !showSolution;
                draw(cd.angle(), 0);
                break;
            case ZOOMIN:
                mapView.incrementMapScale();
                draw(cd.angle(),0);
                break;
            case ZOOMOUT:
                mapView.decrementMapScale();
                draw(cd.angle(),0);
                break;
        }
        return false;
    }

    private synchronized void rotate(int dir) {
        final int originalAngle = cd.angle();//angle;
        final int steps = 4;
        int angle = originalAngle; // just in case for loop is skipped
        for (int i = 0; i != steps; i++) {
            // add 1/4 of 90 degrees per step
            // if dir is -1 then subtract instead of addition
            angle = originalAngle + dir*(90*(i+1))/steps;
            angle = (angle+1800) % 360;
            // draw method is called and uses angle field for direction
            // information.
            slowedDownRedraw(angle, 0);
        }
        // update maze direction only after intermediate steps are done
        // because choice of direction values are more limited.
        cd = CardinalDirection.getDirection(angle);

    }

    private void slowedDownRedraw(int angle, int walkStep) {
        draw(angle, walkStep) ;
        try {
            Thread.sleep(25);
        } catch (Exception e) {
            // may happen if thread is interrupted
            // no reason to do anything about it, ignore exception
        }
    }
    /**
     * Draws the current content on panel to show it on screen.
     * @param angle the current viewing angle, east == 0 degrees, south == 90, west == 180, north == 270
     * @param walkStep a counter for intermediate steps within a single step forward or backward
     */
    protected void draw(int angle, int walkStep) {

        if (panel == null) {
            printWarning();
            return;
        }
        Log.v("draw method", "reached draw");
        // draw the first person view and the map view if wanted
        firstPersonView.draw(panel, px, py, walkStep, angle,
                maze.getPercentageForDistanceToExit(px, py)) ;
        if (isInMapMode()) {
            mapView.draw(panel, px, py, angle, walkStep,
                    isInShowMazeMode(),isInShowSolutionMode()) ;
        }
        // update the screen with the buffer graphics
        panel.commit() ;
    }
    private synchronized void walk(int dir) {
        // check if there is a wall in the way
        if (!wayIsClear(dir))
            return;
        int walkStep = 0;
        // walkStep is a parameter of FirstPersonView.draw()
        // it is used there for scaling steps
        // so walkStep is implicitly used in slowedDownRedraw
        // which triggers the draw operation in
        // FirstPersonView and Map
        for (int step = 0; step != 4; step++) {
            walkStep += dir;
            slowedDownRedraw(cd.angle(), walkStep);
        }
        // update position to neighbor
        int[] tmpDxDy = cd.getDxDyDirection();
        setCurrentPosition(px + dir*tmpDxDy[0], py + dir*tmpDxDy[1]) ;

    }

    protected boolean wayIsClear(int dir) {
        cd = getCurrentDirection();
        return !maze.hasWall(px, py, cd);
    }


    boolean printedWarning = false;
    protected void printWarning() {
        if (printedWarning)
            return;
        System.out.println("StatePlaying.start: warning: no panel, dry-run game without graphics!");
        printedWarning = true;
    }

    private void setShortestDistance(){
        int [] pos = getCurrentPosition();
        shortestPossibleSteps = maze.getDistanceToExit(pos[0], pos[1]);
    }


    ////////////////////////////// set methods ///////////////////////////////////////////////////////////////
    ////////////////////////////// Actions that can be performed on the maze model ///////////////////////////
    protected void setCurrentPosition(int x, int y) {
        px = x ;
        py = y ;
    }
    ////////////////////////////// get methods ///////////////////////////////////////////////////////////////
    protected int[] getCurrentPosition() {
        int[] result = new int[2];
        result[0] = px;
        result[1] = py;
        return result;
    }
    protected CardinalDirection getCurrentDirection() {
        return cd;
    }
    boolean isInMapMode() {
        return mapMode ;
    }
    boolean isInShowMazeMode() {
        return showMaze ;
    }
    boolean isInShowSolutionMode() {
        return showSolution ;
    }
    public Maze getMaze() {
        return maze ;
    }
}