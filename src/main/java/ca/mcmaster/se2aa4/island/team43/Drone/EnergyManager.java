package ca.mcmaster.se2aa4.island.team43.Drone;

import ca.mcmaster.se2aa4.island.team43.Phases.Phase;

public class EnergyManager {
    private int currentBattery;
    private final int distanceToBase = 30;

    //Managing battery levels
    public EnergyManager(int maxBattery){
        this.currentBattery = maxBattery;
    }

    public Phase checkBattery(){
        //Calculate current battery, and whats needed to return to base
        if(currentBattery <= 30){
            return Phase.STOP;
        }
        return Phase.CONTINUE;
    }

    public int getCurrentBattery(){
        return currentBattery;
    }

    public void depleteBattery(int cost){
        currentBattery -= cost;
    }

    
}

