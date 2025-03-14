package ca.mcmaster.se2aa4.island.team43;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.team43.JSONHandler;
import ca.mcmaster.se2aa4.island.team43.CommandCenter;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private final CommandCenter commandCenter = new CommandCenter();
    private final JSONHandler jsonHandler = new JSONHandler(commandCenter);

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
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
        return "no creek found";
    }

}
