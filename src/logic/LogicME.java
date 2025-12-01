package logic;

import structure.models.Cell;
import structure.models.Move;
import structure.models.Position;
import structure.models.State;
import structure.models.Color;

import java.util.*;

public class LogicME {
public static Set<Move> getPossibleMoves(State state) {
    Set<Move> possibleMoves = new HashSet<>();
    for (Move move : Move.values()) {
        Position nextPos = move.getNextPosition(state.getPlayerPosition());
        if (isValidMove(state, nextPos)) {
            possibleMoves.add(move);
        }
    }
    return possibleMoves; // الآن يعيد Set<Move>
}
    private static boolean isValidMove(State state, Position nextPos) {
        Cell nextCell = state.getBoard().get(nextPos);
        if (nextCell == null || nextCell.isCollapsed()) {
            return false;
        }
        if (nextCell.getType() == Cell.CellType.LOCK && state.getCollectedKeys().isEmpty()) {
            return false;
        }
        return true;
    }
    public static State applyMove(State currentState, Move move) {
        Position newPos = move.getNextPosition(currentState.getPlayerPosition());
        Position oldPos = currentState.getPlayerPosition();
        Map<Position, Cell> newBoard = new HashMap<>(currentState.getBoard());
        Map<Position, Color> newCellColors = new HashMap<>(currentState.getCellColors());
        Cell cellToCollapse = currentState.getBoard().get(oldPos);
        Color cellColor = currentState.getCellColors().getOrDefault(newPos, Color.getDefaultColor());
        double currentCost = calculateStepCost(cellColor);

        double newCost = currentState.getAccumulatedCost() + currentCost;
        if (cellToCollapse != null && cellToCollapse.getType() != Cell.CellType.GOAL) {
            Cell collapsedCell = new Cell(cellToCollapse);
            collapsedCell.incrementCollapseCount();
            newBoard.put(oldPos, collapsedCell);
        }
        Cell oldCell = currentState.getBoard().get(newPos);
        Cell newCell = new Cell(oldCell);
        Set<Position> newKeys = new HashSet<>(currentState.getCollectedKeys());
        if (oldCell.getType() == Cell.CellType.KEY) {
            newKeys.add(newPos);
        }
        if (oldCell.getType() == Cell.CellType.LOCK && !currentState.getCollectedKeys().isEmpty()) {
            Iterator<Position> iterator = newKeys.iterator();
            if (iterator.hasNext()) {
                Position keyToRemove = iterator.next();
                iterator.remove();
            }
            newCell = new Cell(Cell.CellType.FLOOR);
        }
        newBoard.put(newPos, newCell);
        return new State(newBoard, newPos, newKeys, currentState, move, newCellColors, newCost);
    }
    private static double calculateStepCost(Color color) {
        return switch (color) {
            case RED -> 5.0;
            case BLUE -> 2.0;
            default -> 1.0; // WHITE
        };
    }
    public static boolean isGoal(State state) {
        Position playerPos = state.getPlayerPosition();
        Cell currentCell = state.getBoard().get(playerPos);

        if (currentCell == null || currentCell.getType() != Cell.CellType.GOAL) {
            return false;
        }

        for (Map.Entry<Position, Cell> entry : state.getBoard().entrySet()) {
            Cell cell = entry.getValue();
            if (cell.getType() != Cell.CellType.GOAL && !cell.isCollapsed()) {
                return false;
            }
        }

        return true;
    }
    public static List<State> getPath(State goalState) {
        List<State> path = new ArrayList<>();
        State current = goalState;
        while (current != null) {
            path.add(current);
            current = current.getParentState();
        }
        Collections.reverse(path);
        return path;
    }

}