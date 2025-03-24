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

    public SearchAlgorithm(Drone drone) {
        this.counter = 0;
        this.foundEmergencySite = false;
        this.drone = drone;
        this.isWidthCentered = false;
        this.isHeightCentered = false;
    }

    public String findDimensions (Map<String, String> parameters) {

        String action;
        if (counter == 0){
            action = "echo";
            parameters.put("direction", "E");
        }
        else if (counter == 1){
            action = "echo";
            parameters.put("direction", "S");
        }
        else{
            action = "COMPLETED PHASE 1";
            counter = -1; // reset counter
        }

        counter += 1;
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
            counter = -1; // reset counter
        }

        counter += 1;
        return action;
    }

    public String spiralSearch(Map<String, String> parameters) {
        // fixed logic for now, but will be replaced with actual search algorithm
        /*
        if (this.foundEmergencySite == true) {
            return "COMPLETED PHASE 3";
        }

        
        String action;
        action = "scan";

        this.counter += 1;
        logger.info("Counter: {}", this.counter);

        return action;
        */

        /*pseudo code for search
        PHASE 3.1: SIZE OF ISLAND

        go all the way north until water is found
        then continue north until scan throws out of bound on both left and right,
        mark this as the top
        turn left, and continue going all the way left until left echo is not returned keep this as the left 
        turn left again, and go until you're past the middle, then start scanning and keep going in similar fashion
        when no more scan is found, then you've reached the end. and we know max width and max height of island

        PHASE 3.2 FIRST SWEEP

        at this point, you are at the bottom left corner
        turn twice, and you'll be within the bounds of the map. for now, for every cell in there, scan the bottom
        when you reach the end of a row, turn left or right (depending on current heading) 2x to make a u turn
        |-> after this always move forward once before going again.
        stop this loop when you've reached either the end if the length of the island is odd, or the second last if even
        if even, do three turns to do the last row that wouldve been missed

        PHASE 3.4 SECOND SWEEP

        then if even, immediately continue in the same fashion, propogating down instead
        if odd, then do the three tunrs to do a u turn.


        Additionally battery life should be watched, and as soon as the battery will die, the return home should be called

        */
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

}