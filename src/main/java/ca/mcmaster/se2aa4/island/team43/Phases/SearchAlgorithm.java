package ca.mcmaster.se2aa4.island.team43.Phases;

import ca.mcmaster.se2aa4.island.team43.Drone.*;
import ca.mcmaster.se2aa4.island.team43.Map.*;
import ca.mcmaster.se2aa4.island.team43.HomeBase.*;


import org.json.*;

import java.util.Map;
import java.util.HashMap;
import java.io.StringReader;
import org.json.JSONObject;
import org.json.JSONTokener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchAlgorithm {

    private int counter; // used to track number of calls to the search algorithm
    private final Logger logger = LogManager.getLogger();
    private int mapWidth;
    private int mapHeight;
    private boolean foundEmergencySite;
    private Drone drone;
    private boolean isWidthCentered;
    private boolean isHeightCentered;

    private int eastBound;
    private int westBound;
    private int northBound;
    private int southBound;
    private boolean validEastBound;
    private boolean validWestBound;
    private boolean validNorthBound;
    private boolean validSouthBound;
    private String previousAction;
    private String previousActionDirection;

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
        this.eastBound = -1;
        this.westBound = -1;
        this.northBound = -1;
        this.southBound = -1;
        this.validEastBound = false;
        this.validWestBound = false;
        this.validNorthBound = false;
        this.validSouthBound = false;
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
        String currentHeading = drone.getCurrentOrientation().toString();

        logger.info("Current counter: {}", counter);
        logger.info("Current bounds validity: E: {}, W: {}, N: {}, S: {}", validEastBound, validWestBound, validNorthBound, validSouthBound);

        // *** PRELIMINARY SCAN FOR DEBUGGING TO SEE START LOCATION -> REMOVE LATER *** //
        if (counter == 0){
            previousAction = "scan";
            previousActionDirection = "N";
            counter += 1;
            return "scan";
        }

        if (currentHeading.equals("E") || currentHeading.equals("W")){
            if (northBound < 0 || (!validNorthBound && previousAction.equals("fly"))){
                action = "echo";
                parameters.put("direction", "N");
                previousAction = action;
                previousActionDirection = "N";
            }
            else if (southBound < 0 || (!validSouthBound && previousAction.equals("fly"))){
                action = "echo";
                parameters.put("direction", "S");
                previousAction = action;
                previousActionDirection = "S";
            }
            else if (!validSouthBound || !validNorthBound){ // move forwards if the bounds aren't valid
                action = "fly";
                previousAction = action;
                if (currentHeading.equals("E")){
                    eastBound -= 1;
                    westBound += 1;
                }
                else {
                    eastBound += 1;
                    westBound -= 1;
                }
            }
            else if (!validEastBound || !validWestBound){
                if (northBound < southBound){
                    action = drone.fly("S", parameters);
                    previousAction = action;
                    previousActionDirection = "S";
                }
                else {
                    action = drone.fly("N", parameters);
                    previousAction = action;
                    previousActionDirection = "N";
                }
            }
            else {
                calculateDimensions();
                drone.setCurrentCoordinate(westBound, northBound);
                action = "COMPLETED PHASE 1";
                counter = -1; // reset counter
            }

        }
        else {
            if (eastBound < 0 || (!validEastBound && previousAction.equals("fly"))){
                action = "echo";
                parameters.put("direction", "E");
                previousAction = action;
                previousActionDirection = "E";
            }
            else if (westBound < 0 || (!validWestBound && previousAction.equals("fly"))){
                action = "echo";
                parameters.put("direction", "W");
                previousAction = action;
                previousActionDirection = "W";
            }
            else if (!validEastBound || !validWestBound){ // move forwards if the bounds aren't valid
                action = "fly";
                previousAction = action;
                if (currentHeading.equals("N")){
                    northBound -= 1;
                    southBound += 1;
                }
                else {
                    northBound += 1;
                    southBound -= 1;
                }
            }
            else if (!validSouthBound || !validNorthBound){
                if (eastBound < westBound){
                    action = drone.fly("W", parameters);
                    previousAction = action;
                    previousActionDirection = "W";
                }
                else {
                    action = drone.fly("E", parameters);
                    previousAction = action;
                    previousActionDirection = "E";
                }
            }
            else {
                calculateDimensions();
                drone.setCurrentCoordinate(westBound, northBound);
                action = "COMPLETED PHASE 1";
                counter = -1; // reset counter
            }
        }

        this.counter += 1;
        return action;
    }

    public void validateBounds(JSONObject extraInfo){

        logger.info("Collecting bounds information...");

        String found;
        int range;

        if (previousAction.equals("echo")){

            found = extraInfo.getString("found");
            range = extraInfo.getInt("range");

            logger.info("Found: {}", found);
            logger.info("Range: {}", range);

            if (previousActionDirection.equals("N")){
                if (found.equals("OUT_OF_RANGE")){
                    validNorthBound = true;
                }
                northBound = range;
            }
            else if (previousActionDirection.equals("S")){
                if (found.equals("OUT_OF_RANGE")){
                    validSouthBound = true;
                }
                southBound = range;
            }
            else if (previousActionDirection.equals("E")){
                if (found.equals("OUT_OF_RANGE")){
                    validEastBound = true;
                }
                eastBound = range;
            }
            else if (previousActionDirection.equals("W")){
                if (found.equals("OUT_OF_RANGE")){
                    validWestBound = true;
                }
                westBound = range;
            }
        }

        logger.info("Finished collecting bounds information...");
    }

    public void calculateDimensions(){
        mapWidth = eastBound + westBound + 1;
        mapHeight = northBound + southBound + 1;
    }

    public String goToCenter (Map<String, String> parameters) {
        String action;
        int halfWidth = (mapWidth / 2) - 1;
        int halfHeight = mapHeight / 2;

        if (counter == 0){
            counter += 1;
            return "scan";
        }

        logger.info("Drone current location: ({}, {})", drone.getCurrentCoordinate().getX(), drone.getCurrentCoordinate().getY());

        logger.info("Map dimensions: ({}, {})", mapWidth, mapHeight);
        if (!isWidthCentered){
            if (drone.getCurrentCoordinate().getX() < halfWidth) {
                logger.info("Moving east...");
                action = drone.fly("E", parameters);
                if (drone.getCurrentCoordinate().getX() >= halfWidth) {
                    isWidthCentered = true;
                }
            } else {
                logger.info("Moving west...");
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
    
    public String goToTopLeft(Map<String, String> parameters) { return goToTopLeft(parameters, true); }
    public String goToTopLeft(Map<String, String> parameters, boolean echoRes) {
        String action;


        if (drone.getCurrentOrientation() == Orientation.NORTH){

            if (this.counter == 0) {
                action = drone.echo("E", parameters);
            } else if (this.counter == 1) {
                action = drone.echo("W", parameters);
            } else if(!echoRes){
                this.islandTop = drone.getCurrentCoordinate().getY() + 1;
                action = drone.fly("W", parameters);
                this.counter = -1;
            } else {
                action = drone.fly("N", parameters);
                this.counter = -1;
            }
        } else if (drone.getCurrentOrientation() == Orientation.WEST) {
            if (this.counter == 0) {
                action = drone.echo("S", parameters);
            }
            if (!echoRes){
                this.islandLeft = drone.getCurrentCoordinate().getX() + 1;
                action = drone.fly("S", parameters);
                this.counter = -1;
            } else {
                action = drone.fly("W", parameters);
                this.counter = -1;
            }
        } else {
            this.counter = -1;
            action = "COMPLETED PHASE 3";
        }

        this.counter++;
        return action;
    }

    //this function essentially goes from top left to bottom right to find dimensions of the island
    //similar to (when facing west) above, echoRes should only be the result from the appropriate echo
    public String getIslandDimension (Map<String, String> parameters) { return getIslandDimension(parameters, true); }
    public String getIslandDimension (Map<String, String> parameters, boolean echoRes) {
        String action;

        if (drone.getCurrentOrientation() == Orientation.SOUTH) {
            //this should be optimized later: tell command not to echo if we havent reached middle yet
            if (this.counter % 2 == 0){
                action = drone.echo("W", parameters);
            } else if (!echoRes) {
                this.islandBottom = drone.getCurrentCoordinate().getY() - 1;
                action = drone.fly("E", parameters);
            } else {
                action = drone.fly("S", parameters);
            }
        } else if (drone.getCurrentOrientation() == Orientation.EAST) {
            if (this.counter % 2 == 0) {
                action = drone.echo("N", parameters);
            }
            if (!echoRes) {
                this.islandRight = drone.getCurrentCoordinate().getX() - 1;
                action = drone.fly("N", parameters);
            } else {
                action = drone.fly("E", parameters);
            }
        } else {
            action = "COMPLETED PHASE 4";
            this.counter = -1;
        }
        this.counter++;
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
        } else {
            String [] moves = {"W", "W", "N", "N", "E", "S", "E"};
            action = drone.fly(moves[this.counter], parameters);
        }

        this.counter++;
        return action;
    }

    public String secondSweep(Map<String, String> parameters){
        String action;
        boolean stop = false;

        Orientation direct = drone.getCurrentOrientation();
        Location currLoc = drone.getCurrentCoordinate();

        if (currLoc.getY() == this.islandBottom + 1) {
            if ((direct == Orientation.EAST) && (currLoc.getX() == this.islandLeft)){
                stop = true;
            } else if ((direct == Orientation.WEST) && (currLoc.getX() == this.islandRight)){
                stop = true;
            }
        }

        if (stop){
            return "COMPLETED PHASE 7";
        }

        if (direct == Orientation.SOUTH) {
            if (currLoc.getX() == this.islandRight + 1) {
                action = drone.fly("W", parameters);
            } else {
                action = drone.fly("E", parameters);
            }
        } else if (direct == Orientation.EAST) {
            if (currLoc.getX() == this.islandRight) {
                action = drone.fly("S", parameters);
            } else {
                action = drone.fly("E", parameters);
            }
        } else {
            if (currLoc.getX() == this.islandLeft) {
                action = drone.fly("S", parameters);
            } else {
                action = drone.fly("W", parameters);
            }
        }
        
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