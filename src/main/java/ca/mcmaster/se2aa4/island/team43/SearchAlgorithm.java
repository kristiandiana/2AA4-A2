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

        counter += 1;
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
            counter = -1; // reset counter
        }

        counter += 1;
        return action;
    }

    public String spiralSearch(Map<String, String> parameters) {
        // fixed logic for now, but will be replaced with actual search algorithm
        if (this.foundEmergencySite == true) {
            return "COMPLETED PHASE 3";
        }

        
        String action;
        action = "scan";

        this.counter += 1;
        logger.info("Counter: {}", this.counter);

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


}