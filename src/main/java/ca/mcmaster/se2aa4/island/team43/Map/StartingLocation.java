package ca.mcmaster.se2aa4.island.team43.Map;

public class StartingLocation extends Location implements POI {
    public StartingLocation(int xIn, int yIn) {
        super(xIn, yIn);
    }

    public String getType() {
        return "start";
    }

    public float getDistance(Location node) {
        return (float) Math.sqrt(Math.pow(this.x - node.x, 2) + Math.pow(this.y - node.y, 2));
    }
    
}
