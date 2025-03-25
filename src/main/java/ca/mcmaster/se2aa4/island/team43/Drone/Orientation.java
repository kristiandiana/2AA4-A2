package ca.mcmaster.se2aa4.island.team43.Drone;

/**
 * The Orientation enum represents the four directions and methods when turning right or left.
 */

public enum Orientation {
    NORTH {
        @Override
        public Orientation turnRight() {
            return EAST;
        }

        @Override
        public Orientation turnLeft() {
            return WEST;
        }

        @Override
        public String toString() {
            return "N";
        }

        @Override
        public boolean opposite(Orientation orientation) {
            return orientation == SOUTH;
        }
    },
    SOUTH {
        @Override
        public Orientation turnRight() {
            return WEST;
        }

        @Override
        public Orientation turnLeft() {
            return EAST;
        }

        @Override
        public String toString() {
            return "S";
        }

        @Override
        public boolean opposite(Orientation orientation) {
            return orientation == NORTH;
        }
    },
    EAST {
        @Override
        public Orientation turnRight() {
            return SOUTH;
        }

        @Override
        public Orientation turnLeft() {
            return NORTH;
        }

        @Override
        public String toString() {
            return "E";
        }

        @Override
        public boolean opposite(Orientation orientation) {
            return orientation == WEST;
        }
    },
    WEST {
        @Override
        public Orientation turnRight() {
            return NORTH;
        }

        @Override
        public Orientation turnLeft() {
            return SOUTH;
        }

        @Override
        public String toString() {
            return "W";
        }

        @Override
        public boolean opposite(Orientation orientation) {
            return orientation == EAST;
        }
    };

    /**
     * Turns the orientation to the right.
     * 
     * @return The new orientation after turning right.
     */
    public abstract Orientation turnRight();

    /**
     * Turns the orientation to the left.
     * 
     * @return The new orientation after turning left.
     */
    public abstract Orientation turnLeft();

    /**
     * Returns the orientation as a string.
     * 
     * @return The orientation as a string.
     */
    public abstract String toString();

    /*
     * Returns if the current is opposite to a given orientation.
     */
    public abstract boolean opposite(Orientation orientation);
}
