package ca.mcmaster.se2aa4.island.team43.HomeBase;

import ca.mcmaster.se2aa4.island.team43.HomeBase.*;
import ca.mcmaster.se2aa4.island.team43.Drone.*;
import ca.mcmaster.se2aa4.island.team43.Map.*;
import ca.mcmaster.se2aa4.island.team43.Phases.*;




import org.json.*;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.StringReader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandCenter {
    //Taking decisions at each step of drone flight
    private final JSONHandler jsonHandler = new JSONHandler();
    private final Logger logger = LogManager.getLogger();
    Drone drone;
    SearchAlgorithm searchAlgorithm;
    private int phase;
    private ArrayList<String> echoRes;

    public CommandCenter(int startBattery, String startOrientation) {

        this.drone = new Drone(startBattery, startOrientation);
        this.searchAlgorithm = new SearchAlgorithm(this.drone);
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
                logger.info("Completed phase 1");
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
            if (echoRes == null){
                action = this.searchAlgorithm.goToTopLeft(parameters);
            } else {
                boolean echoParam = false;
                for (int i = 0; i < echoRes.size(); i++){
                    /* HERE DONT FORGET TO EDIT HERE PLEASE */
                    if (!echoRes.get(i).equals("OUT_OF_BOUNDS")){ echoParam = true;}
                }
                action = this.searchAlgorithm.goToTopLeft(parameters, echoParam);
            }
            if (action.equals("COMPLETED PHASE 3")) {
                echoRes.clear();
                phase = 3; // move to the next phase
            }
        }

        if (phase == 3){
            if (echoRes == null){
                action = this.searchAlgorithm.getIslandDimension(parameters);
            } else if (echoRes.get(0).equals("OUT_OF_BOUNDS")){
            action = this.searchAlgorithm.goToTopLeft(parameters, false);
            } else {
                action = this.searchAlgorithm.goToTopLeft(parameters, true);
            }

            if (action.equals("COMPLETED PHASE 4")) {
                echoRes.clear();
                phase = 4; // move to the next phase
            }
        }

        if (phase == 4) {
            action = this.searchAlgorithm.firstSweep(parameters);
            if (action.equals("COMPLETED PHASE 5")) {
                phase = 5;
            }
        }

        if (phase == 5) {
            action = this.searchAlgorithm.uTurn(parameters);
            if (action.equals("COMPLETED PHASE 6")) {
                phase = 6;
            }
        }

        if (phase == 6) {
            action = this.searchAlgorithm.secondSweep(parameters);
            if (action.equals("COMPLETED PHASE 7")) {
                phase = 7;
            }
        }

        if (phase == 7) {
            action = "stop";
        }
        // SEND THE ACTION AND PARAMETERS TO THE Explorer
        return jsonHandler.createDecision(action, parameters);
    
    }

    public void processData (int cost, String status, JSONObject extraInfo) {
        logger.info("Processing data... command center side");
        /*
            PARAMETERS ARE COMING STRAIGHT FROM THE JSONHANDLER:
            - COST shoud be given to the drone power management
            - STATUS should be given to the MIA check
            - EXTRAINFO should be given to the whoever is keeping track of POIs

         */
        logger.info("The cost of the action was {}", cost); // GIVE TO DRONE POWER MANAGEMENT
        logger.info("The status of the drone is {}", status); // GIVE TO MIA CHECK  
        logger.info("Additional information received: {}", extraInfo); // GIVE TO MAP LOGGER

        drone.depleteBattery(cost);
        logger.info("Current battery level: {}", drone.getCurrentBattery());

        if (phase == 0) { // getting width and height 
            searchAlgorithm.validateBounds(extraInfo);
        }
        else if (phase == 2) {
            if (extraInfo.has("found")) {
                this.echoRes.add(extraInfo.getString("found"));
                if (this.echoRes.size() > 3) {
                    this.echoRes.remove(0);
                }
            }
        } else if (phase == 3) {
            if (extraInfo.has("found")) {
                this.echoRes.add(extraInfo.getString("found"));
                if (this.echoRes.size() > 1) {
                    this.echoRes.remove(0);
                }
            }
        } else if ( (phase == 4) || (phase == 7)) {
            ;
        }
    }

    public String determineFinalReport() {

        // SEND THE FINAL REPORT TO THE EXPLORER

        // NOTE SURE WHAT DATA TO SEND YET... TBD

        return "Mission Complete: message from CommandCenter to Explorer"; 

    }

}
