package ca.mcmaster.se2aa4.island.team43;

import java.util.Map;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Drone {
    private Location currentCoordinate;
    private Orientation currentOrientation;
    private final EnergyManager energyManager;
    private final Location startCoordinate;

    private final Logger logger = LogManager.getLogger();

    public Drone(int maxBattery, String startOrientation){
        //this.startCoordinate = startCoordinate;
        //this.currentCoordinate = startCoordinate;
        this.startCoordinate = new NormalLocation(1, 1);
        this.currentCoordinate = new NormalLocation(1, 1);
        this.currentOrientation = stringToOrientation(startOrientation);
        this.energyManager = new EnergyManager(this, maxBattery);
    }

    public Orientation stringToOrientation(String orientation){
        if (orientation.equals("N")){
            return Orientation.NORTH;
        }
        else if (orientation.equals("E")){
            return Orientation.EAST;
        }
        else if (orientation.equals("S")){
            return Orientation.SOUTH;
        }
        else if (orientation.equals("W")){
            return Orientation.WEST;
        }
        return null;
    }
  
    public Location getCurrentCoordinate(){
        return currentCoordinate;
    }

    public void setCurrentCoordinate(int x, int y){
        Location newCoordinate = new NormalLocation(x, y);
        this.currentCoordinate = newCoordinate;
    }

    public Location getBaseCoordinate(){
        return startCoordinate;
    }

    public Orientation getCurrentOrientation(){
        return currentOrientation;
    }

    public void updateOrientation(Orientation newOrientation){
        this.currentOrientation = newOrientation;
    }

    public String fly(String direction, Map<String, String> parameters){
        String action = new String();
        logger.info("Current orientation: {}", currentOrientation);
        switch(currentOrientation){
            case NORTH:
                if (direction.equals("N")){
                    action = "fly";
                    currentCoordinate = new NormalLocation(currentCoordinate.getX(), currentCoordinate.getY() - 1);
                }
                else if (direction.equals("E")){
                    action = "heading";
                    parameters.put("direction", "E");
                    currentOrientation = currentOrientation.turnRight();
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() + 1, currentCoordinate.getY()-1);
                }
                else if (direction.equals("W")){
                    action = "heading";
                    parameters.put("direction", "W");
                    currentOrientation = currentOrientation.turnLeft();
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() - 1, currentCoordinate.getY()-1);
                }
                else if (direction.equals("S")){
                    action = "heading";
                    parameters.put("direction", "E"); // default to turn right
                    currentOrientation = currentOrientation.turnRight();
                    currentCoordinate = new NormalLocation(currentCoordinate.getX()+1, currentCoordinate.getY() - 1);
                }
                break;
            case EAST:
                if (direction.equals("N")){
                    action = "heading";
                    parameters.put("direction", "N");
                    currentOrientation = currentOrientation.turnLeft();
                    currentCoordinate = new NormalLocation(currentCoordinate.getX()+1, currentCoordinate.getY()-1);
                }
                else if (direction.equals("E")){
                    action = "fly";
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() + 1, currentCoordinate.getY());
                }
                else if (direction.equals("W")){
                    action = "heading";
                    parameters.put("direction", "S");
                    currentOrientation = currentOrientation.turnRight(); // default to turn right
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() + 1, currentCoordinate.getY() + 1);
                }
                else if (direction.equals("S")){
                    action = "heading";
                    parameters.put("direction", "S");
                    currentOrientation = currentOrientation.turnRight();
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() + 1, currentCoordinate.getY() + 1);
                }
                break;
            case SOUTH:
                if (direction.equals("N")){
                    action = "heading";
                    parameters.put("direction", "W");
                    currentOrientation = currentOrientation.turnRight(); // default to turn right
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() - 1, currentCoordinate.getY() + 1);
                }
                else if (direction.equals("E")){
                    action = "heading";
                    parameters.put("direction", "E");
                    currentOrientation = currentOrientation.turnLeft();
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() + 1, currentCoordinate.getY() + 1);
                }
                else if (direction.equals("W")){
                    action = "heading";
                    parameters.put("direction", "W");
                    currentOrientation = currentOrientation.turnRight();
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() - 1, currentCoordinate.getY() + 1);
                }
                else if (direction.equals("S")){
                    action = "fly";
                    currentCoordinate = new NormalLocation(currentCoordinate.getX(), currentCoordinate.getY() + 1);
                }
                break;
            case WEST:
                if (direction.equals("N")){
                    action = "heading";
                    parameters.put("direction", "N");
                    currentOrientation = currentOrientation.turnRight();
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() - 1, currentCoordinate.getY() - 1);
                }
                else if (direction.equals("E")){
                    action = "heading";
                    parameters.put("direction", "N");
                    currentOrientation = currentOrientation.turnRight(); // default to turn right
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() - 1, currentCoordinate.getY() - 1);
                }
                else if (direction.equals("W")){
                    action = "fly";
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() - 1, currentCoordinate.getY());
                }
                else if (direction.equals("S")){
                    action = "heading";
                    parameters.put("direction", "S");
                    currentOrientation = currentOrientation.turnLeft();
                    currentCoordinate = new NormalLocation(currentCoordinate.getX() - 1, currentCoordinate.getY() + 1);
                }
                break;
        }
        return action;
    }

    public void fly(){
        switch(currentOrientation){
            case NORTH:
                currentCoordinate = new NormalLocation(currentCoordinate.getX(), currentCoordinate.getY() - 1);
                break;
            case EAST:
                currentCoordinate = new NormalLocation(currentCoordinate.getX() + 1, currentCoordinate.getY());
                break;
            case SOUTH:
                currentCoordinate = new NormalLocation(currentCoordinate.getX(), currentCoordinate.getY() + 1);
                break;
            case WEST:
                currentCoordinate = new NormalLocation(currentCoordinate.getX() - 1, currentCoordinate.getY());
                break;
        }
    }

    public void depleteBattery(int cost){
        energyManager.depleteBattery(cost);
    }

    public boolean checkBattery(){
        return energyManager.checkBattery();
    }

}
