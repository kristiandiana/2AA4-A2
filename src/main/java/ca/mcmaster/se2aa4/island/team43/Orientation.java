package ca.mcmaster.se2aa4.island.team43;

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
}
