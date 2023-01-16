package maze;

import java.io.*;
import java.util.Scanner;

public class Application {

    private static final Scanner SCANNER = new Scanner(System.in);
    private boolean active;
    private Maze maze;

    private MazeSolver mazeSolver;

    public Application() {
        this.active = true;
        run();
    }

    private void startingMenu() {

        System.out.println("=== Menu ===");
        System.out.println("1. Generate a new maze");
        System.out.println("2. Load a maze");
        System.out.println("0. Exit");
        String input = SCANNER.nextLine();
        switch (input) {
            case "1":
                generateMaze();
                displayMaze();
                break;
            case "2":
                loadMaze();
                break;
            case "0":
                exit();
                break;
            default:
                System.out.println("Incorrect option. Please try again");
        }
    }

    private void mainMenu() {

        System.out.println("=== Menu ===");
        System.out.println("1. Generate a new maze");
        System.out.println("2. Load a maze");
        System.out.println("3. Save the maze");
        System.out.println("4. Display the maze");
        System.out.println("5. Find the escape");
        System.out.println("0. Exit");
        String input = SCANNER.nextLine();
        switch (input) {
            case "1":
                generateMaze();
                displayMaze();
                break;
            case "2":
                loadMaze();
                break;
            case "3":
                saveMaze();
                break;
            case "4":
                displayMaze();
                break;
            case "5":
                solveMaze();
                displayMaze();
                break;
            case "0":
                exit();
                break;
            default:
                System.out.println("Incorrect option. Please try again");
        }
    }

    private void generateMaze() {
        /*
         * User input maze's height (number of rows) and width (number of columns)
         * separated by space
         * */

        System.out.println("Please, enter the size of a maze");
        String input = SCANNER.nextLine();
        int height = Integer.parseInt(input);
        int width = Integer.parseInt(input);
        maze = new Maze(height, width);
    }

    private void loadMaze() {

        Maze backup = maze == null ? null : maze.copyOf();

//        System.out.print("Input filename: ");
        String filename = SCANNER.nextLine();

        File inFile = new File(filename);
        if (inFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inFile))) {
                maze = (Maze) ois.readObject();
            } catch (EOFException | NotSerializableException | ClassNotFoundException e) {
                maze = backup;
                System.out.println("Cannot load the maze. It has an invalid format");
                // e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                maze = backup;
            } finally {
                if (maze != null){
                mazeSolver = new MazeSolver(maze);
                }
            }
        } else {
            System.out.println("The file ... does not exist");
        }
    }

    private void saveMaze() {

//        System.out.print("Input filename: ");
        String filename = SCANNER.nextLine();

        File outFile = new File(filename);
        try {
            outFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outFile))) {
            oos.writeObject(maze);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayMaze() {

        maze.display();
    }

    private void exit() {
        active = false;
        System.out.println("Bye!");
    }

    public void run() {

        while (active) {
            if (maze == null) {
                startingMenu();
            } else {
                mainMenu();
            }
        }
    }

    public void solveMaze(){
        mazeSolver = new MazeSolver(maze);
        mazeSolver.solve();
    }

}