package puzzles.jam.model;

/**
 * A simple implementation of two-dimensional integer-based coordinates.
 * Note that this class is a record, which means
 * <ul>
 *     <li>These objects are immutable.</li>
 *     <li>
 *         Many methods are provided that do not appear in the source.
 *         <ul>
 *             <li>constructor</li>
 *             <li>{@link #equals(Object)}</li>
 *             <li>{@link #hashCode()}</li>
 *             <li>accessors</li>
 *         </ul>
 *     </li>
 * </ul>
 * Java records also supply a toString method, but this is being overridden
 * here in favor of a more concise format.
 *
 * @author RIT CS
 * February 2022
 */
public record Coordinates( int row, int col )
        implements Comparable< Coordinates > {

    /**
     * Initialize this object with strings representing the integer values.
     * {@link Integer#parseInt(String)} is used to parse the strings.
     * @see Coordinates#Coordinates(int, int)
     * @param rowStr row number as a string
     * @param colStr column number as a string
     */
    public Coordinates( String rowStr, String colStr ) {
        this( Integer.parseInt( rowStr ), Integer.parseInt( colStr ) );
    }

    @Override
    public String toString() {
        return "(" + this.row + ',' + this.col + ')';
    }

    /**
     * Determine natural order of two coordinate pairs.
     * This ordering is useful because if you sort a list of coordinates
     * they come out in row-major order.
     * @param o the second pair of coordinates
     * @return negative if this row is less than the other row, or if
     *         the rows are the same, if this column is less than the
     *         other column; 0 if rows <u>and</u> columns are equal;
     *         positive otherwise
     */
    @Override
    public int compareTo( Coordinates o ) {
        int result = this.row - o.row;
        if ( result == 0 ) result = this.col - o.col;
        return result;
    }
}
