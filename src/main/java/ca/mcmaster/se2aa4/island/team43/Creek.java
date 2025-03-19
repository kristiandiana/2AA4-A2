package ca.mcmaster.se2aa4.island.team43;

public class Creek extends Location implements POI  {
    //A POI of pickup spots for rescuers

    public Creek(int xIn, int yIn) {
        super(xIn, yIn);
    }

    public String getType() {
        return "creek";
    }

    public float getDistance(Location node) {
        return (float) Math.sqrt(Math.pow(this.x - node.x, 2) + Math.pow(this.y - node.y, 2));
    }
}
