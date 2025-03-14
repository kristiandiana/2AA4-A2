package ca.mcmaster.se2aa4.island.team43;

import java.util.ArrayList;

public class Island {
    //Store information about the mapping of the island
    //Creeks, Emergency site location, size of map
    Location[][] grid;
    StartingLocation start;
    ArrayList<Creek> creeks;
    EmergencySite emergencySite;

    public Island (int MaxX, int MaxY, StartingLocation startingLocation) {
        this.grid = new Location[MaxX][MaxY];
        this.start = startingLocation;
        this.grid[startingLocation.getX()][startingLocation.getY()] = startingLocation;
        this.creeks = new ArrayList<Creek>();
        this.emergencySite = null;
    }

    public void addCreek(int x, int y) {
        Creek newCreek = new Creek(x, y);
        this.creeks.add(newCreek);
        this.grid[x][y] = newCreek;
    }

    public void addEmergencySite(int x, int y) {
        if (this.emergencySite != null) {
            throw new Error("Cannot have more than one Pick Up Location!");
        }
        EmergencySite newEmergencySite = new EmergencySite(x, y);
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
