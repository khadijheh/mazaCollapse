package structure.models;

import logic.LogicME;

import java.util.Objects;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
public class State implements Comparable<State> {
    private final Map<Position, Cell> board;
    private final Position playerPosition;
    private final Set<Position> collectedKeys;
    private final Map<Position, Color> cellColors;
    private final State parentState;
    private final Move move;
    private String cachedStringRepresentation;
    private final double cost;
    public State(Map<Position, Cell> initialBoard, Position startPosition,Map<Position, Color> initialCellColors) {
        this.board = Collections.unmodifiableMap(initialBoard);
        this.playerPosition = startPosition;
        this.collectedKeys = Collections.emptySet();
        this.cellColors = Collections.unmodifiableMap(initialCellColors);
        this.parentState = null;
        this.move = null;
        this.cost = 0.0;
        this.cachedStringRepresentation = null;
    }
    public State(Map<Position, Cell> newBoard, Position newPlayerPosition, Set<Position> newCollectedKeys, State parentState, Move move, Map<Position, Color> newCellColors,double newCost) {
        this.board = Collections.unmodifiableMap(newBoard);
        this.playerPosition = newPlayerPosition;
        this.collectedKeys = Collections.unmodifiableSet(newCollectedKeys);
        this.cellColors = Collections.unmodifiableMap(newCellColors);
        this.parentState = parentState;
        this.move = move;
        this.cost=newCost;
        this.cachedStringRepresentation = null;
    }
    public double getAccumulatedCost() {
        return cost;
    }
    public Map<Position, Cell> getBoard() {
        return board;
    }
    public Position getPlayerPosition() {
        return playerPosition;
    }
    public Set<Position> getCollectedKeys() {
        return collectedKeys;
    }
    public Map<Position, Color> getCellColors() {
        return cellColors;
    }
    public State getParentState() {
        return parentState;
    }
    public Move getMove() {
        return move;
    }
    @Override
    public String toString() {
        if (cachedStringRepresentation == null) {
            cachedStringRepresentation = calculateStringRepresentation();
        }
        return cachedStringRepresentation;
    }
    private String calculateStringRepresentation() {
        int minX = board.keySet().stream().mapToInt(Position::getX).min().orElse(0);
        int maxX = board.keySet().stream().mapToInt(Position::getX).max().orElse(0);
        int minY = board.keySet().stream().mapToInt(Position::getY).min().orElse(0);
        int maxY = board.keySet().stream().mapToInt(Position::getY).max().orElse(0);
        StringBuilder sb = new StringBuilder();
        sb.append("Move: ").append(move != null ? move.name() : "START").append("\n");
        sb.append("Keys Collected: ").append(collectedKeys.size()).append("\n");
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Position currentPos = new Position(x, y);
                Cell cell = board.get(currentPos);
                if (currentPos.equals(playerPosition)) {
                    sb.append('^');
                } else if (cell != null) {
                    char symbol = cell.getCurrentSymbol();
//                    Color color = cellColors.getOrDefault(currentPos, Color.getDefaultColor());
                    sb.append(symbol);
                } else {
                    sb.append(' ');
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(board, state.board) &&
                Objects.equals(playerPosition, state.playerPosition) &&
                Objects.equals(collectedKeys, state.collectedKeys) &&
                Objects.equals(cellColors, state.cellColors);
    }
    @Override
    public int hashCode() {
        return Objects.hash(board, playerPosition, collectedKeys, cellColors);
    }
    @Override
    public int compareTo(State other) {
        double f_this = this.cost + LogicME.calculateHeuristic(this);
        double f_other = other.cost + LogicME.calculateHeuristic(other);
        int fComparison = Double.compare(f_this, f_other);
        int costComparison = Double.compare(this.cost, other.cost);
        if (fComparison != 0) {
            return fComparison;
        }
        return costComparison;
    }
}
