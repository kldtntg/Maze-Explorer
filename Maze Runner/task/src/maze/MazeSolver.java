package maze;


import java.util.ArrayList;
import java.util.List;

class MazeSolver {
    private final int ROWS;
    private final int COLS;
    private final Cell[][] GRID;
    private boolean[][] visited;
    private boolean solved = false;

    public Maze maze;

    public MazeSolver(Maze maze) {
        this.maze = maze;
        ROWS = maze.ROWS;
        COLS = maze.COLS;
        GRID = maze.GRID;
        visited = new boolean[ROWS][COLS];
    }

    public boolean dfs(int row, int col, List<Cell> path) { //use depth-first search here
        // if out of bounds or current cell is a wall or already visited
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS || !GRID[row][col].isPath() || visited[row][col]) {
            return false;
        }
        visited[row][col] = true;

        // if current cell is the exit
        if (GRID[row][col].isExit()) {
            path.add(GRID[row][col]);
            solved = true;
            return true;
        }

        // recursively explore neighbors
        boolean isPartOfPath = false;
        if (dfs(row - 1, col, path) || dfs(row + 1, col, path) || dfs(row, col - 1, path) || dfs(row, col + 1, path)) {
            isPartOfPath = true;
        }
        if (isPartOfPath) {
            path.add(GRID[row][col]);
        }
        return isPartOfPath;
    }

    public boolean isSolved() {
        return solved;
    }

    public void solve() {
        List<Cell> path = new ArrayList<>();
        dfs(maze.entrance.getX(), maze.entrance.getY(), path);
        if (solved) {
            for (Cell cell : path) {
                cell.setAsSolvedPath();
            }
        }
    }
}