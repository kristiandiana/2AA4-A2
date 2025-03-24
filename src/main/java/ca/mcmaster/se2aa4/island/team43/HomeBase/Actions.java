package ca.mcmaster.se2aa4.island.team43.HomeBase;

public enum Actions {
    FLY{
        @Override
        public String toString() {
            return "fly";
        }
    },
    HEADING{
        @Override
        public String toString() {
            return "heading";
        }
    },
    ECHO{
        @Override
        public String toString() {
            return "echo";
        }
    },
    SCAN{
        @Override
        public String toString() {
            return "scan";
        }
    },
    STOP{
        @Override
        public String toString() {
            return "stop";
        }
    };

    public abstract String toString();
}

