package maze;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class Maze implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int ROWS;
    public final int COLS;
    public final Cell[][] GRID;


    public Maze(int rows, int columns) {

        ROWS = rows;
        COLS = columns;
        GRID = new Cell[ROWS][COLS];
        create();
    }


    public Maze(Cell[][] grid) {
        ROWS = grid.length;
        COLS = grid[0].length;
        GRID = grid;
    }

    private void initGrid() {
        /*
         * Creates initial empty grid
         * Sets left, upper and lower sides of cells as walls
         * Also sets walls at even indexes (corridors)
         * The right side will be set later
         * */

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                GRID[r][c] = new Cell(r, c);
                if (r == 0 || r == ROWS - 1 || c == 0 || r % 2 == 0 && c % 2 == 0) {
                    GRID[r][c].setAsWall();
                }
            }
        }
    }

    boolean entrance_created = false;

    public Cell entrance;
    boolean exit_created = false;
    private void create() {
        /*
         * Creates maze by iterative implementation of
         * 'randomized depth-first search' (recursive backtracker)
         * */

        initGrid();

        Random randomizer = new Random();
        Deque<Cell> uncheckedCells = new ArrayDeque<>();

//////////////////////////////////////////////////////////PRIM's algorithm starts here


        // randomly choose an entrance from the left (first col, 0), but not a corner (hence the +1)
        int startingIndex = randomizer.nextInt(ROWS - 2) + 1;
        Cell currentPath = GRID[startingIndex][0];
        if (!entrance_created){
            entrance = currentPath;
            currentPath.setAsEntrance();
            entrance_created = true;
        }
        uncheckedCells.offer(currentPath); //insert current cell at the end of deque

        while (!uncheckedCells.isEmpty()) {

            currentPath = uncheckedCells.pollLast(); //retrieves and removes the last cell from this deque

            if (connectsPaths(currentPath)) {//checks if the current cell would form a loop if set as path
                currentPath.setAsWall();
                continue;
            } else {
                currentPath.setAsPath();

                // if cell is at the rightmost side
                if (currentPath.getY() == COLS - 1) {
                    if (!exit_created){
                    currentPath.setAsExit();
                    exit_created = true;}
                    // set rest of the side as walls
                    for (int i = 0; i < ROWS; i++) {
                        if (currentPath.getX() != i) {
                            GRID[i][COLS - 1].setAsWall();
                        }
                    }
                }
            }

            // get cell's neighbors (nulls removed)
            // if neighbor is valid, add to stack (uncheckedCells)
            // else set as wall

            // valid neighbor = a neighboring cells which does not lead to a path
            // (thus avoid creating a loop)
            List<Cell> neighbors = cellNeighbors(currentPath);
            List<Cell> validNeighbors = new ArrayList<>();

            for (Cell neighbor : neighbors) {
                if (hasPathNear(neighbor, currentPath)) {
                    neighbor.setAsWall();
                } else if (!uncheckedCells.contains(neighbor)) {
                    uncheckedCells.offer(neighbor);
                    validNeighbors.add(neighbor);
                }
            }

            // select a random neighboring cell to continue the path
            // if none is selected, backtracking occurs
            if (!validNeighbors.isEmpty()) {
                Cell randomCell = validNeighbors.get(randomizer.nextInt(validNeighbors.size()));

                // push it last in stack, to be selected first in the next iteration
                while (uncheckedCells.contains(randomCell)) {
                    uncheckedCells.remove(randomCell);
                }
                uncheckedCells.offer(randomCell);
            }
        }
    }

    private boolean connectsPaths(Cell cell) {
        /**
          Returns true if the cell would create a loop
          if set as path
         */

        int r = cell.getX();
        int c = cell.getY();
        Cell upper = getCellByPos(r - 1, c);  // upper
        Cell lower = getCellByPos(r + 1, c);  // lower
        Cell leftSide = getCellByPos(r, c - 1);  // left
        Cell rightSide = getCellByPos(r, c + 1);  // right


        //since all paths stem from 1 original node, if this cell has any 2 adjacent nodes that are already in a path, it
        // will form a loop if it is made part of the path.
        boolean horizontalConnection = leftSide != null && rightSide != null && leftSide.isPath() && rightSide.isPath();
        boolean verticalConnection = upper != null && lower != null && upper.isPath() && lower.isPath();

        return  horizontalConnection || verticalConnection;
    }

    private boolean hasPathNear(Cell cell, Cell excl) {
        /**
         * returns true if any neighboring cell is visited and marked as path
         * meaning it would create an unwanted loop.
         * The cell the path is continuing from is excluded
         * */

        if (excl == null) {
            return false;
        }

        List<Cell> neighbors = cellNeighbors(cell);
        neighbors.remove(excl);
        for (Cell neighbor : neighbors) {
            if (neighbor.isPath()) {
                return true;
            }
        }
        return false;
    }

    private List<Cell> cellNeighbors(Cell cell) {
        /*
         * Returns a list of the cell's vertical & horizontal direct neighbors
         * */

        int r = cell.getX();
        int c = cell.getY();
        List<Cell> neighbors = new ArrayList<>();
        neighbors.add(getCellByPos(r - 1, c));  // upper
        neighbors.add(getCellByPos(r + 1, c));  // lower
        neighbors.add(getCellByPos(r, c - 1));  // left
        neighbors.add(getCellByPos(r, c + 1));  // right

        neighbors.removeIf(Objects::isNull);
        neighbors.removeIf(Cell::isVisited);

        return neighbors;
    }

    public Cell getCellByPos(int x, int y) {
        /*
         * Returns cell based on its coordinates in the grid
         * or null if out of grid
         * */

        try {
            return GRID[x][y];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void display() {
        /*
         * Text-based representation of the maze
         * */

        System.out.println();
        for (Cell[] row : GRID) {
            for (Cell cell : row) {
                System.out.print(cell.isSolvedPath()? cell.toStringSolved() : cell.toString());
            }
            System.out.println();
        }
        System.out.println();
    }

    public Maze copyOf() {

        Cell[][] newGrid = new Cell[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            if (COLS >= 0) System.arraycopy(GRID[r], 0, newGrid[r], 0, COLS);
        }
        return new Maze(newGrid);
    }


}