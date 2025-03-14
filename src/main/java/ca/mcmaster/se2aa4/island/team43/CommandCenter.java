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
    
    public String getCommand() {
        // SOME LOGIC, IMPLEMENTED ELSEWHERE, IS USED TO DETERMINE THE ACTION AND PARAMETERS
    
        String action;
        Map<String, String> parameters = new HashMap<String, String>();

        // EXAMPLE DATA FOR NOW BUT PUT THE LOGIC / ALGORITHM HERE
        action = "echo";
        parameters.put("direction", "N");
        // action = "stop"; // other test case
        // parameters = null;

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

    }

    public String deliverFinalReport() {

        // SEND THE FINAL REPORT TO THE EXPLORER

        // NOTE SURE WHAT DATA TO SEND YET... TBD

        return "no creek found";

    }

}
