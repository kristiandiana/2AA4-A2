package ca.mcmaster.se2aa4.island.team43;

import org.json.*;

import java.util.Map;
import java.util.HashMap;
import java.io.StringReader;
import org.json.JSONObject;
import org.json.JSONTokener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team43.JSONHandler;

public class SearchAlgorithm {

    private int counter; // used to track number of calls to the search algorithm
    private final Logger logger = LogManager.getLogger();
    private int mapWidth;
    private int mapHeight;
    private boolean foundEmergencySite;
    private Drone drone;
    private boolean isWidthCentered;
    private boolean isHeightCentered;
    private int islandTop;
    private int islandBottom;
    private int islandLeft;
    private int islandRight;


    public SearchAlgorithm(Drone drone) {
        this.counter = 0;
        this.foundEmergencySite = false;
        this.drone = drone;
        this.isWidthCentered = false;
        this.isHeightCentered = false;
    }

    /* SEARCH ALGORITHM HAS 7 PHASES
     * FIND DIMENSIONS
     * GO TO CENTRE
     * GO TO TOP LEFT
     * GET ISLAND DIMENSIONS
     * FIRST SWEEP (interlaced sweep requires two iterations)
     * IF LENGTH OF ISLAND IS EVEN, THEN: U TURN PHASE AND COMPLETE LAST ROW
     * SECOND SWEEP
     */

    public String findDimensions (Map<String, String> parameters) {

        String action;
        if (this.counter == 0){
            action = "echo";
            parameters.put("direction", "E");
        }
        else if (this.counter == 1){
            action = "echo";
            parameters.put("direction", "S");
        }
        else{
            action = "COMPLETED PHASE 1";
            counter = -1; // reset counter
        }

        this.counter += 1;
        return action;
    }

    public String goToCenter (Map<String, String> parameters) {
        String action;
        int halfWidth = (mapWidth / 2) - 1;
        int halfHeight = mapHeight / 2;

        if (!isWidthCentered){
            if (drone.getCurrentCoordinate().getX() < halfWidth) {
                action = drone.fly("E", parameters);
                if (drone.getCurrentCoordinate().getX() >= halfWidth) {
                    isWidthCentered = true;
                }
            } else {
                action = drone.fly("W", parameters);   
                if (drone.getCurrentCoordinate().getX() <= halfWidth) {
                    isWidthCentered = true;
                } 
            }
        }
        else if (!isHeightCentered){
            if (drone.getCurrentCoordinate().getY() < halfHeight) {
                action = drone.fly("S", parameters);
                if (drone.getCurrentCoordinate().getY() >= halfHeight) {
                    isHeightCentered = true;
                }
            } else {
                action = drone.fly("N", parameters);
                if (drone.getCurrentCoordinate().getY() <= halfHeight) {
                    isHeightCentered = true;
                }
            }
        }
        else {
            action = "COMPLETED PHASE 2";
            this.counter = -1; // reset counter
        }

        this.counter += 1;
        return action;
    }
    /*
    pseudo code for search
    SIZE OF ISLAND

    go all the way north until water is found
    then continue north until scan throws out of bound on both left and right,
    mark this as the top
    turn left, and continue going all the way left until left echo is not returned keep this as the left 
    turn left again, and go until you're past the middle, then start scanning and keep going in similar fashion
    when no more scan is found, then you've reached the end. and we know max width and max height of island

    FIRST SWEEP

    at this point, you are at the bottom left corner
    turn twice, and you'll be within the bounds of the map. for now, for every cell in there, scan the bottom
    when you reach the end of a row, turn left or right (depending on current heading) 2x to make a u turn
    |-> after this always move forward once before going again.
    stop this loop when you've reached either the end if the length of the island is odd, or the second last if even
    if even, do three turns to do the last row that wouldve been missed

    SECOND SWEEP

    then if even, immediately continue in the same fashion, propogating down instead
    if odd, then do the three tunrs to do a u turn.


    Additionally battery life should be watched, and as soon as the battery will die, the return home should be called
    */

    //takes in parameter output map, and previous echo response as arguments, returns the action as the output
    // if out of bounds is found both left and right when the droen is facing north, then echoRes should == "none"
    // similar for when drone facign east, except only left
    public String goToTopLeft(Map<String, String> parameters) { return goToTopLeft(parameters, true); }
    public String goToTopLeft(Map<String, String> parameters, boolean echoRes) {
        String action;
        if (drone.getCurrentOrientation() == Orientation.NORTH){
            if (!echoRes){
                this.islandTop = drone.getCurrentCoordinate().getY() + 1;
                action = drone.fly("W", parameters);
            } else {
                action = drone.fly("N", parameters);
            }
        } else if (drone.getCurrentOrientation() == Orientation.WEST) {
            if (!echoRes){
                this.islandLeft = drone.getCurrentCoordinate().getX() + 1;
                action = drone.fly("S", parameters);
            } else {
                action = drone.fly("W", parameters);
            }
        } else {
            action = "COMPLETED PHASE 3";
        }

        return action;
    }

    //this function essentially goes from top left to bottom right to find dimensions of the island
    //similar to (when facing west) above, echoRes should only be the result from the appropriate echo
    public String getIslandDimension (Map<String, String> parameters) { return getIslandDimension(parameters, true); }
    public String getIslandDimension (Map<String, String> parameters, boolean echoRes) {
        String action;

        if (drone.getCurrentOrientation() == Orientation.SOUTH) {
            //this should be optimized later: tell command not to echo if we havent reached middle yet
            if (!echoRes) {
                this.islandBottom = drone.getCurrentCoordinate().getY() - 1;
                action = drone.fly("E", parameters);
            } else {
                action = drone.fly("S", parameters);
            }
        } else if (drone.getCurrentOrientation() == Orientation.EAST) {
            if (!echoRes) {
                this.islandRight = drone.getCurrentCoordinate().getX() - 1;
                action = drone.fly("N", parameters);
            } else {
                action = drone.fly("E", parameters);
            }
        } else {
            action = "COMPLETED PHASE 4";
        }
        return action;
    }

    //this function conducts the sweep after the dimensions are found
    public String firstSweep (Map<String, String> parameters) {
        String action;
        boolean stop = false;

        Orientation direct = drone.getCurrentOrientation();
        Location currLoc = drone.getCurrentCoordinate();

        if (((this.islandBottom - this.islandTop) % 4 == 0) && (currLoc.getY() == this.islandTop - 1) && (currLoc.getX() == this.islandRight)) {
            stop = true;
        } else if (((this.islandBottom - this.islandTop) % 2 == 0) && (currLoc.getY() == this.islandTop - 1) && (currLoc.getX() == this.islandLeft)) {
            stop = true;
        } else if ((currLoc.getY() == this.islandTop)) {

            if (((this.islandBottom - this.islandTop - 1) % 4 == 0) && (currLoc.getX() == this.islandLeft)) {
                stop = true;
            } else if (currLoc.getX() == this.islandRight) {
                stop = true;
            }
            
        }

        if (stop){
            this.counter = 0;
            return "COMPLETED PHASE 5";
        }

        if (direct == Orientation.NORTH) {
            if (currLoc.getX() == this.islandRight + 1) {
                action = drone.fly("E", parameters);
            } else {
                action = drone.fly("W", parameters);
            }
        } else if (direct == Orientation.EAST) {
            if (currLoc.getX() == this.islandRight) {
                action = drone.fly("N", parameters);
            } else {
                action = drone.fly("E", parameters);
            }
        } else if (direct == Orientation.WEST) {
            if (currLoc.getX() == this.islandLeft) {
                action = drone.fly("N", parameters);
            } else {
                action = drone.fly("W", parameters);
            }
        } else {
            if (currLoc.getX() < this.islandLeft) {
                action = drone.fly("E", parameters);
            } else {
                action = drone.fly("W", parameters);
            }
        }
        
        return action;
    }

    public String uTurn(Map<String, String> parameters) {
        String action;

        Location currLoc = drone.getCurrentCoordinate();

        if (this.counter == 7) {
            counter = - 1;
            action = "COMPLETED PHASE 6";
        } else if ((this.getIslandHeight() - 1) % 4 == 0) {
            String [] moves = {"N", "W", "S", "S", "E", "E", "E"};
            action = drone.fly(moves[this.counter], parameters);
        } else if ((this.getIslandHeight() - 1) % 2 == 0) {
            String [] moves = {"N", "E", "S", "S", "W", "W", "W"};
            action = drone.fly(moves[this.counter], parameters);
        } else if (this.getIslandHeight() % 4 == 0) {
            String [] moves = {"E", "E", "N", "N", "W", "S", "W"};
            action = drone.fly(moves[this.counter], parameters);
        } else if (this.getIslandHeight() % 2 == 0) {
            String [] moves = {"W", "W", "N", "N", "E", "S", "E"};
            action = drone.fly(moves[this.counter], parameters)
        }

        this.counter++;
        return action;
    }


    public int getCounter(){
        return this.counter;
    }

    public void setMapWidth(int width){
        this.mapWidth = width;
    }

    public void setMapHeight(int height){
        this.mapHeight = height;
    }

    public int getMapWidth(){
        return this.mapWidth;
    }

    public int getMapHeight(){
        return this.mapHeight;
    }

    public void setFoundEmergencySite(boolean foundEmergencySite){
        this.foundEmergencySite = foundEmergencySite;
    }

    public int getIslandHeight(){
        return (this.islandBottom - this.islandTop);
    }

}