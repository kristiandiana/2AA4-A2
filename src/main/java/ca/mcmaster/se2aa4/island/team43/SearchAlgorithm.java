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

    public SearchAlgorithm() {
        this.counter = 0;
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

        if (counter < halfWidth) {
            action = "fly";
        } else if (counter == halfWidth) {
            action = "heading";
            parameters.put("direction", "S");
        } else if (counter > halfWidth && counter <= halfWidth + halfHeight) {
            action = "fly";
        } else {
            action = "COMPLETED PHASE 2";
            counter = -1; // reset counter
        }

        counter += 1;
        return action;
    }

    public String spiralSearch(Map<String, String> parameters) {
        // fixed logic for now, but will be replaced with actual search algorithm
        if (counter == 2) {
            return "COMPLETED PHASE 3";
        }


        
        String action;
        action = "scan";

        this.counter += 1;

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

}