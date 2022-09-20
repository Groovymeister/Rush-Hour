package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.jam.model.Car;
import puzzles.jam.model.Coordinates;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Class that drives the PTUI.
 *
 * @author John West (jrw2936)
 */
public class JamPTUI extends ConsoleApplication implements Observer<JamModel, String>  {
    private JamModel model;
    private boolean initialized;
    private PrintWriter out;
    private static String filename;
    private char selection;
    private Coordinates initialSelection;

    /**
     * Updates the displayed text configuration
     * @param jamModel Current model.
     * @param msg Message to be printed.
     */
    @Override
    public void update(JamModel jamModel, String msg) {
        if (initialized) {
            boolean won = this.model.getCurrentConfig().isSolution();
            if (won) {
                this.model.setGameState(JamModel.GameState.WON);
                System.out.println("Game won. Please load a new file.");
            }
            if (jamModel.getGameState() != JamModel.GameState.WON) {
                System.out.println(msg);

            }
            drawGrid();
        }
        else {
            System.out.println("Too early.");
            System.exit(2);
        }
    }

    /**
     * Progresses to the next step in the game.
     */
    public void hint(){
        if (model.getGameState() == JamModel.GameState.UNLOADED){
            System.out.println("Please load a file first.");
        }
        else if (model.getGameState() == JamModel.GameState.WON){
            System.out.println("Game won. Please load a new file.");
        }
        else {
            model.setCurrentConfig(model.nextStep());
            update(this.model, "Advancing to the next step.");
        }
    }

    /**
     * Draws the 2-D array with labeled axis.
     */
    public void drawGrid(){
        char[][] grid = model.getCurrentConfig().getGrid();
        System.out.print("   ");
        for (int i = 0; i < model.getCurrentConfig().getCols(); i++){
            if (i != model.getCurrentConfig().getCols() - 1) {
                System.out.print(i + " ");
            }
            else {
                System.out.print(i + System.lineSeparator());
            }
        }
        System.out.print("  ");
        for (int i = 0; i < model.getCurrentConfig().getCols(); i++){
            System.out.print("--");
        }
        System.out.print(System.lineSeparator());
        for (int i = 0; i < model.getCurrentConfig().getRows(); i++){
            System.out.print(i + "| ");
            for (int j = 0; j < model.getCurrentConfig().getCols(); j++){
                if (j != model.getCurrentConfig().getCols() - 1) {
                    System.out.print(grid[i][j] + " ");
                }
                else {
                    System.out.print(grid[i][j]);
                }
            }
            System.out.print(System.lineSeparator());
        }
    }

    /**
     * Loads a new file.
     *
     * @param filename Name of file.
     */
    public void load(String filename) throws IOException {
        JamPTUI.filename = filename;
        this.model.setGameState(JamModel.GameState.ONGOING);
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String[] dimensions = br.readLine().split(" ");
        int numCars = Integer.parseInt(br.readLine());
        ArrayList<Car> cars = new ArrayList<>();
        for (int i = 0; i < numCars; i++) {
            String nextLine = br.readLine();
            if (nextLine != null) {
                String[] temp = nextLine.split(" ");
                cars.add(new Car(temp[0].charAt(0),
                        new Coordinates(Integer.parseInt(temp[1]), Integer.parseInt(temp[2])),
                        new Coordinates(Integer.parseInt(temp[3]), Integer.parseInt(temp[4]))));
            }
        }
        this.model.setCurrentConfig(new JamConfig(Integer.parseInt(dimensions[0]),
                Integer.parseInt(dimensions[1]),
                numCars,
                cars));

        update(this.model, "Loaded: " + filename);
    }

    /**
     * Used for handling user-input selection.
     *
     * @param row Selected row
     * @param col Selected column
     */
    public void select(int row, int col){
        if (this.model.getGameState() == JamModel.GameState.WON){
            System.out.println("Game won. Please load a new file");
        }
        else if (this.model.getGameState() == JamModel.GameState.UNLOADED){
            System.out.println("Please load a file first.");
        }
        else {
            char[][] grid = this.model.getCurrentConfig().getGrid();

            /* Check boundaries first */
            if (!(row >= 0
                    && col >= 0
                    && row <= this.model.getCurrentConfig().getRows() - 1
                    && col <= this.model.getCurrentConfig().getCols() - 1)){
                this.model.setGameState(JamModel.GameState.ILLEGAL_MOVE);
            }

            if (this.model.getGameState() == JamModel.GameState.ILLEGAL_MOVE){
                this.model.setGameState(JamModel.GameState.ONGOING);
                update(this.model, "Illegal move!");
            }

            else if (this.model.getGameState() == JamModel.GameState.ONGOING){
                if (grid[row][col] == '.'){
                    update(this.model, "No car selected at (" + row + ", " + col + ")");
                }

                else {
                    this.selection = grid[row][col];
                    this.model.setGameState(JamModel.GameState.SELECTED_CELL);
                    this.initialSelection = new Coordinates(row, col);
                    update(this.model, "Selected car " + selection + " at (" + row + ", " + col + ")");
                }

            }

            else if (this.model.getGameState() == JamModel.GameState.SELECTED_CELL){
                Car currentCar = this.model.getCurrentConfig().selectCar(selection);
                if (!this.model.checkSide(currentCar, new Coordinates(row, col)) ||
                        (currentCar.isHorizontal() && currentCar.getStart().row() != row ||
                                !currentCar.isHorizontal() && currentCar.getStart().col() != col)){
                    this.model.setGameState(JamModel.GameState.ONGOING);
                    if (!((currentCar.isHorizontal() && currentCar.getStart().row() != row) ||
                            !currentCar.isHorizontal() && currentCar.getStart().col() != col)) {
                        update(this.model, "Can't move to " + new Coordinates(row, col));
                    }
                    else {
                        update(this.model, "Illegal move!");
                    }
                }
                else {
                    this.model.moveCar(currentCar, new Coordinates(row, col));
                    this.model.setGameState(JamModel.GameState.ONGOING);
                    update(this.model, "Car moved from " + initialSelection.toString() +
                            " to " + "(" + row + ", " + col + ")");
                }
            }
        }
    }

    /**
     * Resets the game
     */
    public void reset() throws IOException {
        this.out.println("Resetting the game.");
        this.model.setGameState(JamModel.GameState.ONGOING);
        load(filename);
    }

    /**
     * Initializes the program.
     *
     * @param out Used for commands
     */
    public void start(PrintWriter out) {
        this.initialized = true;
        this.model = new JamModel();
        this.model.addObserver( this );
        this.out = out;
        try {
            load(filename);
        }
        catch (IOException e){
            this.model.setGameState(JamModel.GameState.UNLOADED);
        }
        super.setOnCommand("h", 0, ": completes next move",
                commandArgs -> this.hint());
        super.setOnCommand("hint", 0, ": completes next move",
                commandArgs -> this.hint());

        super.setOnCommand("l", 1, " <filename>: loads the specified file",
                commandArgs -> {
                    try {
                        this.load(commandArgs[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        super.setOnCommand("load", 1, " <filename>: loads the specified file",
                commandArgs -> {
                    try {
                        this.load(commandArgs[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        super.setOnCommand("s", 2, " <row> <column>: selects the cell at the position",
                commandArgs -> this.select(Integer.parseInt(commandArgs[0]), Integer.parseInt(commandArgs[1])));
        super.setOnCommand("select", 2, " <row> <column>: selects the cell at the position",
                commandArgs -> this.select(Integer.parseInt(commandArgs[0]), Integer.parseInt(commandArgs[1])));

        super.setOnCommand("r", 0, ": resets the game",
                commandArgs -> {
                    try {
                        this.reset();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        super.setOnCommand("reset", 0, ": resets the game",
                commandArgs -> {
                    try {
                        this.reset();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        super.setOnCommand("q", 0, ": quits the game",
                commandArgs -> System.exit(0));
    }

    /**
     * Main method that drives the program.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        }
        else {
            filename = args[0];
            launch(JamPTUI.class, args);
        }
    }
}
