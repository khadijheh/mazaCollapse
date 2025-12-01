package structure.models;

import java.util.Objects;

public class Cell {
    public enum CellType {
        FLOOR('_'),
        START('^'),
        GOAL('G'),
        KEY('K'),
        LOCK('L'),
        DOUBLE_PASS('D');
        private final char symbol;

        CellType(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
    }

    private final CellType type;
    private int collapseCount;

    public Cell(CellType type) {
        this.type = type;
        this.collapseCount = 0;
    }

    public Cell(Cell other) {
        this.type = other.type;
        this.collapseCount = other.collapseCount;
    }

    public CellType getType() {
        return type;
    }

    public void incrementCollapseCount() {
        this.collapseCount++;
    }

    public boolean isCollapsed() {
        if (type == CellType.DOUBLE_PASS) {
            return collapseCount >= 2;
        }
        return collapseCount >= 1;
    }

    public char getCurrentSymbol() {
        if (isCollapsed()) {
            return ' ';
        }
        if (type == CellType.DOUBLE_PASS && collapseCount == 1) {
            return 'd';
        }
        return type.getSymbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return collapseCount == cell.collapseCount && type == cell.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, collapseCount);
    }
}