package ca.mcmaster.se2aa4.island.team43;

public class Drone {
    private Location currentCoordinate;
    private Orientation currentOrientation;
    private final EnergyManager energyManager;
    private final Location startCoordinate;

    public Drone(Location startCoordinate, Orientation startOrientation, int maxBattery){
        this.startCoordinate = startCoordinate;
        this.currentCoordinate = startCoordinate;
        this.currentOrientation = startOrientation;
        this.energyManager = new EnergyManager(this, maxBattery);
    }

    public Location getCurrentCoordinate(){
        return currentCoordinate;
    }

    public Orientation getCurrentOrientation(){
        return currentOrientation;
    }

    public void updateOrientation(Orientation newOrientation){
        this.currentOrientation = newOrientation;
    }

    public void fly(){
        switch(currentOrientation){
            case NORTH:
                currentCoordinate = new Location(currentCoordinate.getX(), currentCoordinate.getY() - 1);
                break;
            case EAST:
                currentCoordinate = new Location(currentCoordinate.getX() + 1, currentCoordinate.getY());
                break;
            case SOUTH:
                currentCoordinate = new Location(currentCoordinate.getX(), currentCoordinate.getY() + 1);
                break;
            case WEST:
                currentCoordinate = new Location(currentCoordinate.getX() - 1, currentCoordinate.getY());
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
