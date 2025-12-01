package structure.models;

public enum Move {
    UP, DOWN, LEFT, RIGHT;
    public Position getNextPosition(Position current) {
        int x = current.getX();
        int y = current.getY();
        if (this == Move.UP) {
            return new Position(x, y - 1);
        } else if (this == Move.DOWN) {
            return new Position(x, y + 1);
        } else if (this == Move.LEFT) {
            return new Position(x - 1, y);
        } else if (this == Move.RIGHT) {
            return new Position(x + 1, y);
        }
        return current;
    }
}
