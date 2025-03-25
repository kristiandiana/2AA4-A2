package ca.mcmaster.se2aa4.island.team43.Map;

import ca.mcmaster.se2aa4.island.team43.Drone.Orientation;

public class Location {
    //Location of drone on map
    private int x;
    private int y;
    
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


    /* Moves the drone in the x direction
     * 
     * @param currentOrientation the current orientation of the drone
     */
    public void moveX(Orientation currentOrientation){
        if(currentOrientation == Orientation.EAST){
            increaseX();
        }
        else if(currentOrientation == Orientation.WEST){
            decreaseX();
        }
    }

    //HELPER METHODS FOR moveX()

    //Increases the x coordinate by 1
    private void increaseX() {
        this.x++;
    }

    //Decreases the x coordinate by 1
    private void decreaseX() {
        this.x--;
    }


    /* Moves the drone in the y direction
     * 
     * @param currentOrientation the current orientation of the drone
     */
    public void moveY(Orientation currentOrientation){
        if(currentOrientation == Orientation.NORTH){
            increaseY();
        }
        else if(currentOrientation == Orientation.SOUTH){
            decreaseY();
        }
    }

    //HELPER METHODS FOR moveY()

    //Increases the y coordinate by 1
    private void increaseY() {
        this.y++;
    }

    //Decreases the y coordinate by 1
    private void decreaseY() {
        this.y--;
    }

    /* Moves the drone in the direction of the new orientation
     * 
     * @param currentOrientation the current orientation of the drone
     * @param newOrientation the new orientation of the drone
     */
    public void move(Orientation currentOrientation, Orientation newOrientation){
        
        if(currentOrientation == newOrientation){
            
            if(currentOrientation == Orientation.NORTH || currentOrientation == Orientation.SOUTH){
                moveY(currentOrientation);
            } else {
                moveX(currentOrientation);
            }
        } else {
            moveX(currentOrientation);
            moveY(newOrientation);
        }
    }

}
