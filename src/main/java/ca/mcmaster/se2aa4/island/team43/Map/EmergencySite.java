package ca.mcmaster.se2aa4.island.team43.Map;

import ca.mcmaster.se2aa4.island.team43.Map.Location;
import ca.mcmaster.se2aa4.island.team43.Map.POI;




public class EmergencySite extends Location implements POI  {
    //A POI of pickup spots for rescuers

    public EmergencySite(int xIn, int yIn) {
        super(xIn, yIn);
    }

    public String getType() {
        return "emergency";
    }

    public float getDistance(Location node) {
        return (float) Math.sqrt(Math.pow(this.x - node.x, 2) + Math.pow(this.y - node.y, 2));
    }
}
