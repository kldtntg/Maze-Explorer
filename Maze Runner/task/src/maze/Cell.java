package maze;

import java.io.Serializable;

public class Cell implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String WALL_SYMBOL = "\u2588\u2588";
    private static final String PATH_SYMBOL = "  "; //2 spaces

    private static final String SOLVED_SYMBOL = "//";
    private final int x;
    private final int y;
    private boolean isPath;

    private boolean isSolvedPath = false;
    private boolean visited;

    private boolean entrance;
    private boolean exit;

    public Cell(int x, int y, boolean isPath) {
        this.x = x;
        this.y = y;
        this.isPath = isPath;
        this.visited = false;
    }

    public Cell(int x, int y) {
        this(x, y, false);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isPath() {
        return isPath;
    }

    public boolean isSolvedPath() {
        return isSolvedPath;
    }

    public void setAsPath() {
        this.isPath = true;
        this.visited = true;
    }

    public void setAsSolvedPath() {
        isSolvedPath = true;
    }

    public void setAsWall() {
        this.isPath = false;
        this.visited = true;
    }

    public void setAsEntrance(){
        this.entrance = true;
    }

    public void setAsExit(){
        this.exit = true;
    }

    public boolean isEntrance() {
        return entrance;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isVisited() {
        return visited;
    }

    @Override
    public String toString() {
        return isPath() ? PATH_SYMBOL : WALL_SYMBOL;
    }

    public String toStringSolved(){
        return SOLVED_SYMBOL;
    }
}