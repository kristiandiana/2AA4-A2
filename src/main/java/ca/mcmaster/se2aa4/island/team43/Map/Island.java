package ca.mcmaster.se2aa4.island.team43.Map;

import java.util.ArrayList;

public class Island {
    //Store information about the mapping of the island
    //Creeks, Emergency site location, size of map
    Location[][] grid;
    ArrayList<Creek> creeks;
    EmergencySite emergencySite;

    public Island (int MaxX, int MaxY) {
        this.grid = new Location[MaxX][MaxY];
        this.creeks = new ArrayList<Creek>();
        this.emergencySite = null;
    }

    public void addLocation(int x, int y) {
        addLocation("N/A", x, y, "N/A");
    }
    
    public void addLocation(String type, int x, int y, String id) {
        if (type.equals("creek")) {
            addCreek(x, y, id);
        } else if (type.equals("emergency")) {
            addEmergencySite(x, y, id);
        } else {
            addNormalLocation(x, y);
        }
    }

    private void addNormalLocation(int x, int y) {
        Location newLocation = new Location(x, y);
        this.grid[x][y] = newLocation;
    }

    private void addCreek(int x, int y, String id) {
        Creek newCreek = new Creek(x, y, id);
        this.creeks.add(newCreek);
        this.grid[x][y] = newCreek;
    }

    private void addEmergencySite(int x, int y, String id) {
        if (this.emergencySite != null) {
            throw new Error("Cannot have more than one Pick Up Location!");
        }
        EmergencySite newEmergencySite = new EmergencySite(x, y, id);
        this.emergencySite = newEmergencySite;
        this.grid[x][y] = newEmergencySite;
    }

    public boolean foundEmergencySite() {
        if (this.emergencySite == null) {
            return false;
        } else {
            return true;
        }
    }
}
