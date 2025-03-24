package ca.mcmaster.se2aa4.island.team43.HomeBase;

import org.json.*;

import java.util.Map;
import java.io.StringReader;
import org.json.JSONObject;
import org.json.JSONTokener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team43.HomeBase.*;

public class JSONHandler {

    private final Logger logger = LogManager.getLogger();
    private CommandCenter commandCenter;

    public JSONHandler(CommandCenter commandCenter) {
        this.commandCenter = commandCenter;
    }

    public JSONHandler() {
        this.commandCenter = null;
    }

    // Write into JSON file
    public String createDecision(String action, Map<String, String> parameters) {
        JSONObject decision = new JSONObject();
        decision.put("action", action);
        
        if (parameters != null && !parameters.isEmpty()) {
            JSONObject paramObject = new JSONObject(parameters);
            decision.put("parameters", paramObject);
        }
        
        logger.info("** Decision created: {}", decision.toString());
        return decision.toString();
    }
    
    // Read from JSON file
    public void processResponse(String responseString) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(responseString)));
        logger.info("** Response received:\n{}", response.toString(2));
        
        // SEND ALL THE FOLLOWING TO THE COMMANDCENTER TO GBE PROCESSED

        int cost = response.getInt("cost");
        //logger.info("The cost of the action was {}", cost); // GIVE TO DRONE POWER MANAGEMENT
        
        String status = response.getString("status");
        //logger.info("The status of the drone is {}", status); // GIVE TO MIA CHECK
        
        JSONObject extraInfo = response.getJSONObject("extras");
        //logger.info("Additional information received: {}", extraInfo); // GIVE TO MAP LOGGER

        logger.info("Processing data...");
        commandCenter.processData(cost, status, extraInfo);

    }

}
