package ca.mcmaster.se2aa4.island.team43.Map;


public class EmergencySite extends Location implements POI  {
    //A POI of pickup spots for rescuers

    String id;
    public EmergencySite(int xIn, int yIn, String idIn) {
        super(xIn, yIn);
        this.id = idIn;
    }

    public String getType() {
        return "emergency";
    }

    public String getId() {
        return this.id;
    }

    public int getDistance(Location node) {
        return (int) Math.abs(this.getX() - node.getX()) + Math.abs(this.getY() - node.getY());
    }
}
