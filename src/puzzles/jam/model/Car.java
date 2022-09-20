package puzzles.jam.model;

import java.util.Objects;

/**
 * Car class used to represent cars in the Jam puzzle.
 *
 * @author John West (jrw2936@rit.edu)
 */
public class Car {
    final char name;
    final Coordinates start;
    final Coordinates end;
    boolean horizontal;
    int length;

    /**
     * Constructor for Car objects.
     *
     * @param name Character representation of the car
     * @param start The starting coordinates of the car
     * @param end The ending coordinates of the car
     */
    public Car(char name, Coordinates start, Coordinates end){
        this.name = name;
        this.start = start;
        this.end = end;
        this.horizontal = start.row() == end.row();
        if (horizontal){
            this.length = (end.col() + 1) - start.col();
        }
        else {
            this.length = start.row() + 1 - end.col();
        }
    }

    /**
     * Returns the starting coordinates of the car.
     *
     * @return Starting coordinates of the car.
     */
    public Coordinates getStart(){
        return this.start;
    }

    /**
     * Returns the ending coordinates of the car.
     *
     * @return Ending coordinates of the car.
     */
    public Coordinates getEnd(){
        return this.end;
    }

    /**
     * Returns boolean of whether the car is horizontal.
     *
     * @return Whether the car is horizontal.
     */
    public boolean isHorizontal(){
        return this.horizontal;
    }

    /**
     * Returns the name, representing the character representation of the car.
     *
     * @return The character representation of the car.
     */
    public char getName(){
        return this.name;
    }

    /**
     * Compares two Car objects to see if they're the same.
     *
     * @param other Object for comparison.
     * @return Whether or not the car objects are equal.
     */
    @Override
    public boolean equals(Object other){
        boolean result = false;
        if (other instanceof Car otherCar){
            if (name == otherCar.name && start.equals(otherCar.start) && end.equals(otherCar.end) &&
                    horizontal == otherCar.horizontal && length == otherCar.length){
                result = true;
            }
        }
        return result;
    }

    /**
     * Returns the hashcode of Car objects.
     *
     * @return Hashcode of Car object.
     */
    @Override
    public int hashCode(){
        return Objects.hashCode(this.name) + Objects.hashCode(this.start) + Objects.hashCode(this.end)
                + Objects.hashCode(this.length);
    }

}
