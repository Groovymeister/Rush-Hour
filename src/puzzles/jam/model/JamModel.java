package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Model class used for GUI and PTUI of Jam.
 *
 * @author John West (jrw2936)
 */
public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private JamConfig currentConfig;

    public enum GameState {ONGOING, WON, UNLOADED, ILLEGAL_MOVE, SELECTED_CELL}

    private GameState gameState;

    /**
     * Changes the current gamestate
     *
     * @param gameState gamestate to be updated to
     */
    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }

    /**
     * Gets the current gamestate
     *
     * @return The current gamestate
     */
    public GameState getGameState(){
        return this.gameState;
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * Sets the current configuration
     *
     * @param config New configuration
     */
    public void setCurrentConfig(JamConfig config){
        this.currentConfig = config;
    }

    /**
     * Advances to the next step. Useful for hint() functionality.
     *
     * @return JamConfig of next step in BFS search.
     */
    public JamConfig nextStep(){
        Collection<Configuration> path = Solver.solve(this.currentConfig);
        ArrayList<Configuration> finalPath = null;
        if (path != null) {
            finalPath = new ArrayList<>(path);
        }
        if (finalPath != null) {
            this.currentConfig = (JamConfig) finalPath.get(1);
            return (JamConfig) finalPath.get(1);
        }
        return null;
    }

    /**
     * Utility method that functions as an isValid for moves. Useful for moves greater than one unit.
     *
     * @param car Current car object
     * @param cell Target coordinates
     * @return
     */
    public boolean checkSide(Car car, Coordinates cell){
        if (currentConfig.getGrid()[cell.row()][cell.col()] != '.' &&
                currentConfig.getGrid()[cell.row()][cell.col()] != car.name){
            return false;
        }

        if (car.isHorizontal()){
            if (cell.col() > car.getEnd().col()){
                for (int i = car.getEnd().col() + 1; i < cell.col(); i++){
                    if (!(currentConfig.getGrid()[car.getEnd().row()][i] == '.')){
                        return false;
                    }
                }
            }
            else if (cell.col() < car.getEnd().col()){
                for (int i = cell.col(); i < car.getStart().col(); i++){
                    if (!(currentConfig.getGrid()[car.getStart().row()][i] == '.')) {
                        return false;
                    }
                }
            }
            else {
                return false;
            }
        }
        else {
            if (cell.row() > car.getEnd().row()){
                for (int i = car.getEnd().row() + 1; i < cell.row(); i++){
                    if (!(currentConfig.getGrid()[i][car.getEnd().col()] == '.')){
                        return false;
                    }
                }
            }
            else if (cell.row() < car.getEnd().row()){
                for (int i = cell.row(); i < car.getStart().row(); i++){
                    if (!(currentConfig.getGrid()[i][car.getStart().col()] == '.')){
                        return false;
                    }
                }
            }
            else {
                return false;
            }
        }
        return true;
    }

    public void moveCar(Car car, Coordinates cell){
        ArrayList<Car> cars = new ArrayList<>(this.currentConfig.getCars());
        if (car.isHorizontal()) {
            if (cell.col() > car.getEnd().col()) {
                int difference = cell.col() - car.getEnd().col();
                cars.set(cars.indexOf(car), new Car(car.name,
                        new Coordinates(car.start.row(), car.start.col() + difference),
                        new Coordinates(car.end.row(), car.end.col() + difference)));
            } else if (cell.col() < car.getEnd().col()) {
                int difference = car.getStart().col() - cell.col();
                cars.set(cars.indexOf(car), new Car(car.name,
                        new Coordinates(car.start.row(), car.start.col() - difference),
                        new Coordinates(car.end.row(), car.end.col() - difference)));
            }
        }

        else {
            if (cell.row() > car.getEnd().row()){
                int difference = cell.row() - car.getEnd().row();
                cars.set(cars.indexOf(car), new Car(car.name,
                        new Coordinates(car.start.row() + difference, car.start.col()),
                        new Coordinates(car.end.row() + difference, car.end.col())));
            }

            else if (cell.row() < car.getEnd().row()){
                int difference = car.getStart().row() - cell.row();
                cars.set(cars.indexOf(car), new Car(car.name,
                        new Coordinates(car.start.row() - difference, car.start.col()),
                        new Coordinates(car.end.row() - difference, car.end.col())));

            }
        }
        this.currentConfig = new JamConfig(currentConfig.getRows(), currentConfig.getCols(), currentConfig.getNumCars(), cars);
    }

    public JamConfig getCurrentConfig(){
        return this.currentConfig;
    }
}
