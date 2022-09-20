package puzzles.jam.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import puzzles.common.Observer;
import puzzles.jam.model.Car;
import puzzles.jam.model.Coordinates;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Class that drives the GUI.
 *
 * @author John West (jrw2936)
 */
public class JamGUI extends Application  implements Observer<JamModel, String>  {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private String filename;
    private JamModel model;
    private ArrayList<String> COLORS= new ArrayList<>(
            Arrays.asList("-fx-background-color:#FF0000","-fx-background-color:#9171be","-fx-background-color:#5dd61c",
                    "-fx-background-color:#e598c5", "-fx-background-color:#c621ae","-fx-background-color:#f41e74",
                    "-fx-background-color:#9de9cb","-fx-background-color:#9fc6f1","-fx-background-color:#99a621",
                    "-fx-background-color:#fad002","-fx-background-color:#e6b2ad","-fx-background-color:#034b79",
                    "-fx-background-color:#2ac9ef","-fx-background-color:#10a533","-fx-background-color:#422278",
                    "-fx-background-color:#4beeb5","-fx-background-color:#4750ed","-fx-background-color:#3e8b89"));
    private final static int ICON_SIZE = 75;
    private char selection;
    private Coordinates initialSelection;
    private Label message = new Label("");
    private BorderPane borderPane = new BorderPane();

    /**
     * Initializes the GUI.
     */
    public void init() throws IOException {
        this.model = new JamModel();
        this.filename = getParameters().getRaw().get(0);
        model.addObserver(this);
        Collections.shuffle(COLORS);
        load(this.filename);
    }

    /**
     * Advances the game to the next move.
     */
    public void hint(){
        if (model.getGameState() == JamModel.GameState.UNLOADED){
            update(this.model, "Please load a file first.");
        }
        else if (model.getGameState() == JamModel.GameState.WON){
            update(this.model,"Game won. Please load a new file.");
        }
        else {
            model.setCurrentConfig(model.nextStep());
            update(this.model, "Advancing to the next step.");
        }
    }

    /**
     * Resets the game to its initial configuration.
     */
    public void reset() throws IOException {
        this.model.setGameState(JamModel.GameState.ONGOING);
        load(this.filename);
        update(this.model, "Reset the game.");
    }

    /**
     * Used for reloading the file on reset or opening a new file.
     *
     * @param filename Name of file
     * @throws IOException
     */
    public void load(String filename) throws IOException {
        this.filename = filename;
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
     * Creates the initial GUI and its children.
     *
     * @param stage Stage for the GUI
     */
    @Override
    public void start(Stage stage) throws Exception {

        BorderPane bp = new BorderPane();
        Scene scene = new Scene(bp);
        this.borderPane = bp;

        Label message = new Label("Loaded: " + filename);
        HBox container = new HBox(message);
        container.setAlignment(Pos.TOP_CENTER);
        bp.setTop(container);
        this.message = message;

        GridPane buttons = new GridPane();
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        bp.setBottom(buttons);

        Button load = new Button("Load");
        load.setOnAction(event -> {
            try {
                String name = this.filename;
                FileChooser fileChooser = new FileChooser();
                String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
                currentPath += File.separator + "data" + File.separator + "jam";
                fileChooser.setInitialDirectory(new File(currentPath));
                File file = fileChooser.showOpenDialog(stage);
                if (file != null){
                    name = "data/jam/" + file.getName();
                }
                load(name);
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttons.add(load, 0, 0);

        Button reset = new Button("Reset");
        reset.setOnAction(event -> {
            try {
                reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        buttons.add(reset, 1, 0);

        Button hint = new Button("Hint");
        hint.setOnAction(event -> hint());
        buttons.add(hint, 2, 0);

        GridPane center = new GridPane();
        bp.setCenter(center);
        center.setAlignment(Pos.CENTER);
        for (int i = 0; i < this.model.getCurrentConfig().getRows(); i++){
            for (int j = 0; j < this.model.getCurrentConfig().getCols(); j++) {
                Button temp = new Button(Character.toString(this.model.getCurrentConfig().getGrid()[i][j]));
                if (this.model.getCurrentConfig().getGrid()[i][j] == '.') {
                    temp.setText(" ");
                }
                temp.setMinSize(ICON_SIZE, ICON_SIZE);
                temp.setMaxSize(ICON_SIZE, ICON_SIZE);
                center.add(temp, j, i);
                int finalJ = j;
                int finalI = i;
                temp.setOnAction(event -> select(finalI, finalJ));
                if (this.model.getCurrentConfig().getGrid()[i][j] !='.'){
                    ArrayList<Car> tempCars = this.model.getCurrentConfig().getCars();
                    String color = COLORS.get(tempCars.indexOf(
                            this.model.getCurrentConfig().selectCar(this.model.getCurrentConfig().getGrid()[i][j])));
                    temp.setStyle(color);

                }

            }
        }

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Selects a cell and handles movement.
     *
     * @param row Row of selected cell
     * @param col Column of selected cell
     */
    public void select(int row, int col){
        if (this.model.getGameState() == JamModel.GameState.WON){
            update(model, "Game won. Please reset or load a new file.");
        }
        else if (this.model.getGameState() == JamModel.GameState.UNLOADED){
            update(model, "Please load a new file first.");
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
     * Updates the GUI accordingly.
     *
     * @param jamModel The model
     * @param msg Message to be displayed at the top
     */
    @Override
    public void update(JamModel jamModel, String msg) {
        message.setText(msg);
        GridPane center = new GridPane();
        borderPane.setCenter(center);
        center.setAlignment(Pos.CENTER);
        for (int i = 0; i < this.model.getCurrentConfig().getRows(); i++){
            for (int j = 0; j < this.model.getCurrentConfig().getCols(); j++) {
                Button temp = new Button(Character.toString(this.model.getCurrentConfig().getGrid()[i][j]));
                if (this.model.getCurrentConfig().getGrid()[i][j] == '.') {
                    temp.setText(" ");
                }
                temp.setMinSize(ICON_SIZE, ICON_SIZE);
                temp.setMaxSize(ICON_SIZE, ICON_SIZE);
                center.add(temp, j, i);
                int finalJ = j;
                int finalI = i;
                temp.setOnAction(event -> select(finalI, finalJ));
                if (this.model.getCurrentConfig().getGrid()[i][j] !='.'){
                    ArrayList<Car> tempCars = this.model.getCurrentConfig().getCars();
                    String color = COLORS.get(tempCars.indexOf(
                            this.model.getCurrentConfig().selectCar(this.model.getCurrentConfig().getGrid()[i][j])));
                    temp.setStyle(color);
                }
            }
        }
        if (model.getCurrentConfig().isSolution()){
            model.setGameState(JamModel.GameState.WON);
            message.setText("Game won. Please reset or load a new file.");
        }
    }

    /**
     * Main function that launches the program.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
