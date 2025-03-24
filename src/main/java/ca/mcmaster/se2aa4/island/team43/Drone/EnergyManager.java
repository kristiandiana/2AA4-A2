package ca.mcmaster.se2aa4.island.team43.Drone;

import ca.mcmaster.se2aa4.island.team43.Drone.Drone;


public class EnergyManager {
    private int currentBattery;
    private final Drone drone;

    //Managing battery levels
    public EnergyManager(Drone drone, int maxBattery){
        this.currentBattery = maxBattery;
        this.drone = drone;
    }

    public boolean checkBattery(){
        //Calculate current battery, and whats needed to return to base
        int distanceToBase = (drone.getCurrentCoordinate().getX() - drone.getBaseCoordinate().getX()) + (drone.getCurrentCoordinate().getY() - drone.getBaseCoordinate().getY());
        return currentBattery > distanceToBase * 6 * 6;
    }

    public int getCurrentBattery(){
        return currentBattery;
    }

    public void depleteBattery(int cost){
        currentBattery -= cost;
    }

    
}

