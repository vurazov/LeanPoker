package com.lineate.xonix.mind.model;

public class Cell {
    private final CellType cellType;
    private final Integer botId;
    private static final Cell EMPTY = new Cell(CellType.EMPTY, -1);
    private static final Cell BORDER = new Cell(CellType.BORDER, -1);

    private Cell(CellType cellType, int botId) {
        this.cellType = cellType;
        this.botId = botId;
    }

    public static Cell empty() {
        return EMPTY;
    }

    public static Cell border() {
        return BORDER;
    }

    public static Cell owned(int pid) {
        return new Cell(CellType.OWNED, pid);
    }

    public static Cell tail(int pid) {
        return new Cell(CellType.TAIL, pid);
    }

    public CellType getCellType() {
        return cellType;
    }

    public Integer getBotId() {
        return botId;
    }

    public Cell copy() {
        return new Cell(this.cellType, this.botId);
    }

    @Override
    public String toString() {
        switch (this.getCellType()) {
            case EMPTY:
                return "Empty";
            case BORDER:
                return "Border";
            case OWNED:
                return "Owned(" + this.getBotId() + ")";
            case TAIL:
                return "Tail(" + this.getBotId() + ")";
        }
        return "";
    }
}