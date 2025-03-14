package ca.mcmaster.se2aa4.island.team43;

public abstract class Location {
    //Location of drone on map
    int x;
    int y;
    
    public Location (int xIn, int yIn) {
        this.x = xIn;
        this.y = yIn;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    abstract String getType();
}
