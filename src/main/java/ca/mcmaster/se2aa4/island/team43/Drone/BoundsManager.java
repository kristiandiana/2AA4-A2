package ca.mcmaster.se2aa4.island.team43.Drone;

import ca.mcmaster.se2aa4.island.team43.HomeBase.*;
import ca.mcmaster.se2aa4.island.team43.Map.*;

import java.util.Map;
import java.util.HashMap;


public class BoundsManager {
    private int eastBound;
    private int westBound;
    private int northBound;
    private int southBound;
    private boolean boundsSet;

    public BoundsManager() {
        this.eastBound = 0;
        this.westBound = 0;
        this.northBound = 0;
        this.southBound = 0;
        this.boundsSet = false;
        
    }

    public Actions validateFly(Location currentLocation, Map<String, String> parameters){
        
        // if the bounds are not set yet, then the check does not need to be performed
        if (!boundsSet){
            return Actions.FLY;
        }

        // check if redirection is required
        String direction = parameters.get("direction");
        
        if (direction.equals("N") && (currentLocation.getY() - northBound) <= 1){
            if ((eastBound - currentLocation.getX()) > (currentLocation.getX() - westBound)){
                parameters.put("direction", "E");
                return Actions.HEADING;
            } else {
                parameters.put("direction", "W");
                return Actions.HEADING;
            }
        }
        else if (direction.equals("S") && (southBound - currentLocation.getY()) <= 1){
            if ((eastBound - currentLocation.getX()) > (currentLocation.getX() - westBound)){
                parameters.put("direction", "E");
                return Actions.HEADING;
            } else {
                parameters.put("direction", "W");
                return Actions.HEADING;
            }
        }
        else if (direction.equals("E") && (eastBound - currentLocation.getX())<= 1){
            if ((currentLocation.getY() - northBound) > (southBound - currentLocation.getY())){
                parameters.put("direction", "N");
                return Actions.HEADING;
            } else {
                parameters.put("direction", "S");
                return Actions.HEADING;
            }
        }
        else if (direction.equals("W") && (currentLocation.getX() - westBound) <= 1){
            if ((currentLocation.getY() - northBound) > (southBound - currentLocation.getY())){
                parameters.put("direction", "N");
                return Actions.HEADING;
            } else {
                parameters.put("direction", "S");
                return Actions.HEADING;
            }
        }
        else {
            return Actions.FLY;
        }
    }


    public void setBoundsSet(boolean boundsSet) {
        this.boundsSet = boundsSet;
    }

    public void setSouthBound(int southBound) {
        this.southBound = southBound;
    }

    public void setEastBound(int eastBound) {
        this.eastBound = eastBound;
    }
}
