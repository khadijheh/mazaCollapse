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
    // LogicME.java
    private static double calculateStepCost(Color color) {
        return switch (color) {
            case RED -> 5.0;
            case YELLOW -> 4.0;  // ⬅️ يجب إضافة هذا
            case GREEN -> 3.0;   // ⬅️ يجب إضافة هذا
            case BLUE -> 2.0;
            default -> 1.0;      // لـ WHITE
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
    // LogicME.java - أضف هذا التابع في أي مكان داخل الكلاس
    public static void printCostGrid(State state, int rows, int cols) {
        System.out.println("\n--- Cost Distribution Grid (Cost to Enter Cell) ---");
        // يتم تمرير 10, 10 كأبعاد من UserPlay أو حيثما يتم استدعاء التابع
        for (int y = 1; y <= rows; y++) {
            for (int x = 1; x <= cols; x++) {
                Position currentPos = new Position(x, y);

                // التحقق مما إذا كانت الخلية هي موقع اللاعب الحالي
                if (currentPos.equals(state.getPlayerPosition())) {
                    System.out.print("  ^  "); // عرض رمز اللاعب بدلاً من التكلفة
                    continue;
                }

                // 1. الحصول على لون الخلية، أو اللون الافتراضي (WHITE)
                Color cellColor = state.getCellColors().getOrDefault(currentPos, Color.getDefaultColor());

                // 2. حساب التكلفة لهذا اللون
                double cost = calculateStepCost(cellColor);

                // 3. طباعة التكلفة بتنسيق عشري واحد (مثل 5.0) مع مسافة ثابتة
                // إذا كانت الخلية ليست جزءًا من اللوحة (null cell)، لا تعرض شيئًا أو 0.0
                if (state.getBoard().containsKey(currentPos)) {
                    System.out.printf(" %1.1f ", cost);
                } else {
                    System.out.print("     "); // مسافة فارغة للخلايا غير الموجودة في اللوحة
                }
            }
            System.out.println(); // سطر جديد لكل صف
        }
        System.out.println("-------------------------------------------------");
    }
    public static double calculateHeuristic(Position currentPos, Position goalPos) {
        int dx = Math.abs(currentPos.getX() - goalPos.getX());
        int dy = Math.abs(currentPos.getY() - goalPos.getY());
        return (double) (dx + dy);
    }
}