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
    private boolean validVerticalBounds;
    private boolean validHorizontalBounds;
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
        this.validVerticalBounds = false;
        this.validHorizontalBounds = false;
    }

    public String findDimensions (Map<String, String> parameters) {

        String action;
        String currentHeading = drone.getCurrentOrientation().toString();

        if (currentHeading.equals("E") || currentHeading.equals("W")){
            if (northBound < 0){
                action = "echo";
                parameters.put("direction", "N");
                previousAction = action;
                previousActionDirection = "N";
            }
            else if (southBound < 0){
                action = "echo";
                parameters.put("direction", "S");
                previousAction = action;
                previousActionDirection = "S";
            }
            else if (validVerticalBounds == false){
                action = "fly"; // move forwards if the bounds aren't valid
                previousAction = action;
            }
            else if (validHorizontalBounds == false){
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
                action = "COMPLETED PHASE 1";
                counter = -1; // reset counter
            }
        }
        else {
            if (eastBound < 0){
                action = "echo";
                parameters.put("direction", "E");
                previousAction = action;
                previousActionDirection = "E";
            }
            else if (westBound < 0){
                action = "echo";
                parameters.put("direction", "W");
                previousAction = action;
                previousActionDirection = "W";
            }
            else if (validHorizontalBounds == false){
                action = "fly"; // move forwards if the bounds aren't valid
                previousAction = action;
            }
            else if (validVerticalBounds == false){
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
                action = "COMPLETED PHASE 1";
                counter = -1; // reset counter
            }
        }

        /*
        if (counter == 0){
            action = "echo";
            parameters.put("direction", "W");
        }
        else if (counter == 1){
            action = "echo";
            parameters.put("direction", "S");
        }
        else{
            action = "COMPLETED PHASE 1";
            counter = -1; // reset counter
        }
        */

        counter += 1;
        return action;
    }

    public void validateBounds(JSONObject extraInfo){
        String found = extraInfo.getString("found");
        int range = extraInfo.getInt("range");

        if (previousAction.equals("echo")){
            if (previousActionDirection.equals("N")){
                if (!found.equals("OUT_OF_RANGE")){
                    validVerticalBounds = false;
                }
                else {
                    validVerticalBounds = true;
                }
                northBound = range;
            }
            else if (previousActionDirection.equals("S")){
                if (!found.equals("OUT_OF_RANGE")){
                    validVerticalBounds = false;
                }
                else {
                    validVerticalBounds = true;
                }
                southBound = range;
            }
            else if (previousActionDirection.equals("E")){
                if (!found.equals("OUT_OF_RANGE")){
                    validHorizontalBounds = false;
                }
                else {
                    validHorizontalBounds = true;
                }
                eastBound = range;
            }
            else if (previousActionDirection.equals("W")){
                if (!found.equals("OUT_OF_RANGE")){
                    validHorizontalBounds = false;
                }
                else {
                    validHorizontalBounds = true;
                }
                westBound = range;
            }
        }
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

    public void setValidVerticalBounds(boolean validVerticalBounds){
        this.validVerticalBounds = validVerticalBounds;
    }

    public void setValidHorizontalBounds(boolean validHorizontalBounds){
        this.validHorizontalBounds = validHorizontalBounds;
    }

}