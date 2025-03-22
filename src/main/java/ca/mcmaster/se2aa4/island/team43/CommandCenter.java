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

public class CommandCenter {
    //Taking decisions at each step of drone flight
    private final JSONHandler jsonHandler = new JSONHandler();
    private final Logger logger = LogManager.getLogger();
    Drone drone;
    SearchAlgorithm searchAlgorithm;
    private int phase;

    public CommandCenter() {

        // this.drone = new Drone();
        this.searchAlgorithm = new SearchAlgorithm();
        this.phase = 0;

        // SHOULD ENERGY MANAGER BE INSTANTIATED HERE?
        // EnergyManager energyManager = new EnergyManager(drone, 1000);
    }
    
    public String getCommand() {
        // SOME LOGIC, IMPLEMENTED ELSEWHERE, IS USED TO DETERMINE THE ACTION AND PARAMETERS
    
        String action = new String();
        Map<String, String> parameters = new HashMap<String, String>();

        // EXAMPLE DATA FOR NOW BUT PUT THE LOGIC / ALGORITHM HERE
        if (phase == 0) {
            action = this.searchAlgorithm.findDimensions(parameters);
            if (action.equals("COMPLETED PHASE 1")) {
                phase = 1; // move to the next phase

                // *** IMPORTANT ***
                // MIGHT BE GOOD TO INITIALIZE THE ISLAND HERE AS MAX WIDTH AND HEIGHT ARE NOW KNOWN IN THE SEARCH ALGORITHM OBJECT

            }
        } 
        if (phase == 1) {
            action = this.searchAlgorithm.goToCenter(parameters);
            if (action.equals("COMPLETED PHASE 2")) {
                phase = 2; // move to the next phase
            }
        }
        if (phase == 2) {
            action = this.searchAlgorithm.spiralSearch(parameters);
            if (action.equals("COMPLETED PHASE 3")) {
                phase = 3; // move to the next phase
            }
        }
        if (phase == 3){
            action = "stop";
            parameters = null;
        }

        // SEND THE ACTION AND PARAMETERS TO THE Explorer
        return jsonHandler.createDecision(action, parameters);
    
    }

    public void processData (int cost, String status, JSONObject extraInfo) {

        /*
            PARAMETERS ARE COMING STRAIGHT FROM THE JSONHANDLER:
            - COST shoud be given to the drone power management
            - STATUS should be given to the MIA check
            - EXTRAINFO should be given to the whoever is keeping track of POIs

         */
        logger.info("The cost of the action was {}", cost); // GIVE TO DRONE POWER MANAGEMENT
        logger.info("The status of the drone is {}", status); // GIVE TO MIA CHECK  
        logger.info("Additional information received: {}", extraInfo); // GIVE TO MAP LOGGER

        if (phase == 0) { // getting width and height 
            if (searchAlgorithm.getCounter() == 1){ // get the width from the extraInfo
                int width = extraInfo.getInt("range");
                // logger.info("The width of the island is {}", width);
                searchAlgorithm.setMapWidth(width);
            }
            else if (searchAlgorithm.getCounter() == 2){ // get the height from the extraInfo
                int height = extraInfo.getInt("range");
                // logger.info("The height of the island is {}", height);
                searchAlgorithm.setMapHeight(height);
            }
        }


    }

    public String determineFinalReport() {

        // SEND THE FINAL REPORT TO THE EXPLORER

        // NOTE SURE WHAT DATA TO SEND YET... TBD

        return "Mission Complete: message from CommandCenter to Explorer"; 

    }

}
