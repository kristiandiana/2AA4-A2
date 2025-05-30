package ca.mcmaster.se2aa4.island.team43.HomeBase;



import java.io.BufferedReader;
import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.team43.Drone.*;
import ca.mcmaster.se2aa4.island.team43.HomeBase.*;


public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private CommandCenter commandCenter;
    private JSONHandler jsonHandler;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        int batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
        this.commandCenter = new CommandCenter(batteryLevel, direction);
        this.jsonHandler = new JSONHandler(commandCenter);
    }

    @Override
    public String takeDecision() {

        return commandCenter.getCommand(); // returns formatting JSON string -> done by JSONHandler
    }

    @Override
    public void acknowledgeResults(String s) {
        jsonHandler.processResponse(s); // retrieves information from JSON string s -> sends to CommandCenter
    }

    @Override
    public String deliverFinalReport() {
        
        String finalReport = commandCenter.determineFinalReport();
        return finalReport; // get the final report from CommandCenter
    }

}
