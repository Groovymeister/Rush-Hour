package puzzles.jam.model;

import puzzles.common.solver.Configuration;

import java.util.*;

/**
 * Configuration class used to solve the Jam puzzle.
 *
 * @author John West (jrw2936@rit.edu)
 */
public class JamConfig implements Configuration {
    private final int rows;
    private final int cols;
    private final int numCars;
    private ArrayList<Car> cars;
    private char[][] grid;

    /**
     * Constructor for JamConfig objects
     * @param rows Number of rows
     * @param cols Number of columns
     * @param numCars Number of cars on the board
     * @param cars ArrayList containing all car objects on the board
     */
    public JamConfig(int rows, int cols, int numCars, ArrayList<Car> cars) {
        this.rows = rows;
        this.cols = cols;
        this.numCars = numCars;
        this.cars = cars;
        makeGrid();
    }

    /**
     * Returns the car with the matching character
     *
     * @param name The character representation of the car
     *
     * @return Car w/ specified name
     */
    public Car selectCar(char name){
        for (Car car : cars){
            if (car.name == name){
                return car;
            }
        }
        return null;
    }

    /**
     * Returns the grid representation of the configuration
     *
     * @return 2-D char array representing the configuration
     */
    public char[][] getGrid(){
        return this.grid;
    }

    /**
     * Returns the number of rows in the configuration.
     *
     * @return Number of rows in the configuration
     */
    public int getRows(){
        return this.rows;
    }

    /**
     * Returns the number of columns in the configuration.
     *
     * @return Number of columns in the configuration
     */
    public int getCols(){
        return this.cols;
    }

    /**
     * Returns the number of cars in the configuration.
     *
     * @return Number of cars in the configuration
     */
    public int getNumCars(){
        return this.numCars;
    }

    /**
     * Creates the 2-D array used to represent the configurations in text.
     */
    public void makeGrid() {
        this.grid = new char[rows][cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.grid[i][j] = '.';
            }
        }
        for (Car car : cars) {
            if (car.horizontal) {
                for (int i = car.start.col(); i < car.end.col() + 1; i++) {
                    this.grid[car.start.row()][i] = car.name;
                }
            } else {
                for (int i = car.start.row(); i < car.end.row() + 1; i++) {
                    this.grid[i][car.start.col()] = car.name;
                }
            }
        }
    }

    /**
     * Returns a string of the 2-D array representing the config
     *
     * @return String of the 2-D array
     */
    @Override
    public String toString() {
        StringBuilder configuration = new StringBuilder();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                configuration.append(this.grid[i][j]).append(" ");
            }
            configuration.append(System.lineSeparator());
        }
        return configuration.toString();
    }

    /**
     * Returns the string hashcode of the configuration by hashing all car objects.
     *
     * @return The string hashcode of the configuration.
     */
    @Override
    public int hashCode() {
        return this.cars.hashCode();
    }

    /**
     * Returns boolean containing whether the solution is achieved.
     *
     * @return Whether the start value of the configuration is the ending value.
     */
    public boolean isSolution() {
        for (Car car : cars) {
            if (car.name == 'X') {
                if (car.end.col() == cols - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines whether two CrossingConfigs are the same
     *
     * @param other The other CrossingConfig
     * @return Whether the configs are the same
     */

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof JamConfig otherConfig) {
            result = this.cars.equals(otherConfig.cars);
        }
        return result;
    }

    /**
     * Returns the configuration's ArrayList of Car objects.
     *
     * @return ArrayList of Car objects
     */
    public ArrayList<Car> getCars (){
        return this.cars;
    }

    /**
     * Returns a Hashset of valid successors.
     *
     * @return A HashSet of successor configurations.
     */
    public Collection<Configuration> getNeighbors() {
        HashSet<Configuration> neighbors = new HashSet<>();
        for (Car car : cars) {
            if (car.horizontal) {
                if (car.start.col() - 1 >= 0){
                    if (this.grid[car.start.row()][car.start.col() - 1] == '.') {
                        ArrayList<Car> cars = new ArrayList<>(this.cars);
                        cars.set(cars.indexOf(car), new Car(car.name,
                                new Coordinates(car.start.row(), car.start.col() - 1),
                                new Coordinates(car.end.row(), car.end.col() - 1)));
                        neighbors.add(new JamConfig(this.rows, this.cols, this.numCars, cars));
                    }
                }
                if (car.end.col() + 1 <= this.cols - 1){
                    if (this.grid[car.end.row()][car.end.col() + 1] == '.') {
                        ArrayList<Car> cars = new ArrayList<>(this.cars);
                        cars.set(cars.indexOf(car), new Car(car.name,
                                new Coordinates(car.start.row(), car.start.col() + 1),
                                new Coordinates(car.end.row(), car.end.col() + 1)));
                        neighbors.add(new JamConfig(this.rows, this.cols, this.numCars, cars));
                        }
                    }
                }
            if (!car.horizontal) {
                if (car.start.row() - 1 >= 0){
                    if (this.grid[car.start.row() - 1][car.start.col()] == '.') {
                        ArrayList<Car> cars = new ArrayList<>(this.cars);
                        cars.set(cars.indexOf(car), new Car(car.name,
                                new Coordinates(car.start.row() - 1, car.start.col()),
                                new Coordinates(car.end.row() - 1, car.end.col())));
                        neighbors.add(new JamConfig(this.rows, this.cols, this.numCars, cars));
                    }
                }
                if (car.end.row() + 1 <= this.rows - 1){
                    if (this.grid[car.end.row() + 1][car.end.col()] == '.') {
                        ArrayList<Car> cars = new ArrayList<>(this.cars);
                        cars.set(cars.indexOf(car), new Car(car.name,
                                new Coordinates(car.start.row() + 1, car.start.col()),
                                new Coordinates(car.end.row() + 1, car.end.col())));
                        neighbors.add(new JamConfig(this.rows, this.cols, this.numCars, cars));

                    }
                }
                }
            }
        return neighbors;
        }
    }
