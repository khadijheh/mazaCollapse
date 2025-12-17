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
            case RED -> 3.0;
            case DARK -> 10.0;
            case BLUE -> 5.0;
            default -> 1.0;
        };
    }
    public static boolean isGoal(State state) {
        Position playerPos = state.getPlayerPosition();
        Cell currentCell = state.getBoard().get(playerPos);
        if (currentCell == null) {
            return false;
        }
//        if (currentCell == null || currentCell.getType() != Cell.CellType.GOAL) {
//            return false;
//        }

//        for (Map.Entry<Position, Cell> entry : state.getBoard().entrySet()) {
//            Cell cell = entry.getValue();
//            if (cell.getType() != Cell.CellType.GOAL && !cell.isCollapsed()) {
//                return false;
//            }
//        }

        return currentCell.getType() == Cell.CellType.GOAL;
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
    public static void printCostGrid(State state, int rows, int cols) {
        System.out.println("\n--- Cost Distribution Grid (Cost to Enter Cell) ---");
        for (int y = 1; y <= rows; y++) {
            for (int x = 1; x <= cols; x++) {
                Position currentPos = new Position(x, y);

                if (currentPos.equals(state.getPlayerPosition())) {
                    System.out.print("  ^  ");
                    continue;
                }

                Color cellColor = state.getCellColors().getOrDefault(currentPos, Color.getDefaultColor());
                double cost = calculateStepCost(cellColor);
                if (state.getBoard().containsKey(currentPos)) {
                    System.out.printf(" %1.1f ", cost);
                } else {
                    System.out.print("     ");
                }
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------------");
    }
    private static double manhattanDistance(Position p1, Position p2) {
        int dx = Math.abs(p1.getX() - p2.getX());
        int dy = Math.abs(p1.getY() - p2.getY());
        return (double) (dx + dy);
    }
    public static double calculateHeuristic(State state) {
        Position GOAL_POSITION = new Position(5, 1);
        Position playerPos = state.getPlayerPosition();
        Position keyPos = null;
        Position lockPos = null;

        for (Map.Entry<Position, Cell> entry : state.getBoard().entrySet()) {
            if (entry.getValue().getType() == Cell.CellType.KEY) {
                keyPos = entry.getKey();
            }
            if (entry.getValue().getType() == Cell.CellType.LOCK) {
                lockPos = entry.getKey();
            }
        }
        if (keyPos != null && lockPos != null && state.getCollectedKeys().isEmpty()) {
            double h_player_to_key = manhattanDistance(playerPos, keyPos);
            double h_key_to_lock = manhattanDistance(keyPos, lockPos);
            double h_lock_to_goal = manhattanDistance(lockPos, GOAL_POSITION);
            return h_player_to_key + h_key_to_lock + h_lock_to_goal;
        }
        return manhattanDistance(playerPos, GOAL_POSITION);
    }
}