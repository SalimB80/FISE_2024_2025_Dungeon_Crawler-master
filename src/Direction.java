public enum Direction {
    EAST(3),
    NORTH(2),
    SOUTH(0),
    STOP(0),
    WEST(1),
    NORTH_EAST(2),
    NORTH_WEST(2),
    SOUTH_EAST(0),
    SOUTH_WEST(0);

    private final int frameLineNumber;

    Direction(int frameLineNumber) {
        this.frameLineNumber = frameLineNumber;
    }

    public int getFrameLineNumber() {
        return frameLineNumber;
    }
}