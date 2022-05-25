import java.util.Arrays;

/**
 * A utility class for the fox hound program.
 * <p>
 * It contains helper functions to check the state of the game
 * board and validate board coordinates and figure positions.
 */
public class FoxHoundUtils {

    // ATTENTION: Changing the following given constants can 
    // negatively affect the outcome of the auto grading!

    /**
     * Default dimension of the game board in case none is specified.
     */
    public static final int DEFAULT_DIM = 8;
    /**
     * Minimum possible dimension of the game board.
     */
    public static final int MIN_DIM = 4;
    /**
     * Maximum possible dimension of the game board.
     */
    public static final int MAX_DIM = 26;

    /**
     * Symbol to represent a hound figure.
     */
    public static final char HOUND_FIELD = 'H';
    /**
     * Symbol to represent the fox figure.
     */
    public static final char FOX_FIELD = 'F';

    /**
     * locates the initial positions of the pieces(fox and hounds)
     *
     * @param dimension size of the board
     * @return a string representing the initial positions of fox and hounds on the board
     * @throws IllegalArgumentException if the given dimension is invalid
     */
    public static String[] initialisePositions(int dimension) {
        if (dimension < 0)
            throw new IllegalArgumentException("Error : Dimensions cannot be a negative number!");
        if (dimension < FoxHoundUtils.MIN_DIM || dimension > FoxHoundUtils.MAX_DIM)
            throw new IllegalArgumentException("Error : Dimension should be between 4 and 26(inclusive)!");
        String[] positions = new String[dimension / 2 + 1];
        if (dimension == 8) {
            String[] pos = {"B1", "D1", "F1", "H1", "E8"}; // default player array
            positions = Arrays.copyOf(pos, pos.length);
        } else {
            char column;
            String coordinates;
            for (int i = 0; i < dimension / 2; ++i) {
                column = (char) (66 + 2 * i);
                coordinates = Character.toString(column) + '1';
                positions[i] = coordinates;
            }
            if (dimension % 2 == 0)
                if ((dimension / 2) % 2 == 0)
                    column = (char) (65 + dimension / 2);
                else
                    column = (char) (64 + dimension / 2);
            else if ((dimension / 2) % 2 == 0)
                column = (char) (66 + dimension / 2);
            else
                column = (char) (65 + dimension / 2);
            coordinates = Character.toString(column) + dimension;
            positions[dimension / 2] = coordinates;
        }
        return positions;
    }

    /**
     * Checks if fox have won the game
     *
     * @param foxPos a string containing the position of fox on the board
     * @return a boolean indicating whether the fox have won or not
     */
    public static boolean isFoxWin(String foxPos) {
        boolean check_fox_won = false;
        if (foxPos.charAt(1) == '1' && foxPos.length() == 2)
            check_fox_won = true;
        return check_fox_won;
    }

    /**
     * Checks if hounds have won the game
     *
     * @param players   an array containing the positions of fox and hounds
     * @param dimension size of the board
     * @return a boolean value indicating whether hounds have won the game or not
     * @throws IllegalArgumentException if the given dimension is invalid
     * @throws NullPointerException     if player array is null
     */
    public static boolean isHoundWin(String[] players, int dimension) {
        if (dimension < 0)
            throw new IllegalArgumentException("Error : Dimension cannot be negative!");
        if (players == null)
            throw new NullPointerException("Error : Strings containing the positions cannot be empty!");
        boolean check_hounds_won = true;
        String co_ord;
        for (int i = 1; i <= dimension; ++i) {
            for (char ch = 'A'; ch <= (char) (64 + dimension); ++ch) {
                co_ord = ch + Integer.toString(i);
                if (isValidMove(dimension, players, 'F', players[dimension / 2], co_ord)) {
                    check_hounds_won = false;
                    break;
                }
            }
        }
        return check_hounds_won;
    }

    /**
     * Checks if the move given by the user is valid
     *
     * @param players an array containing the positions of fox and hounds
     * @param dim     size of the board
     * @param figure  a character indicating the piece(fox or hound) to be moved
     * @param origin  a string representing the initial position of the piece
     * @param dest    a string representing the destination(final position) of the piece
     * @return a boolean value indicating whether the move is valid or not
     * @throws IllegalArgumentException if the given dimension is invalid
     * @throws NullPointerException     if player array is null
     */
    public static boolean isValidMove(int dim, String[] players, char figure, String origin, String dest) {
        if (dim < 0)
            throw new IllegalArgumentException("Error : Dimensions cannot be a negative number!");
        if (dim < FoxHoundUtils.MIN_DIM || dim > FoxHoundUtils.MAX_DIM)
            throw new IllegalArgumentException("Error : Dimension should be between 4 and 26(inclusive)!");
        if (players == null)
            throw new NullPointerException("Error : Array including positions of fox and hounds cannot be empty!");
        boolean validity = false;
        int check = 1, i;
        if (figure == 'F') {
            if (origin.equals(players[dim / 2])) {
                if (65 <= dest.charAt(0) && dest.charAt(0) <= (char) (64 + dim) && (((dest.charAt(0) - origin.charAt(0)) == 1) || ((dest.charAt(0) - origin.charAt(0)) == -1))) {
                    if (dim < 10) {
                        if ((0 < (dest.charAt(1) - 48)) && ((int) (dest.charAt(1)) - 48 <= dim) && (((dest.charAt(1) - origin.charAt(1)) == -1) || ((dest.charAt(1) - origin.charAt(1)) == 1))) {
                            for (i = 0; i <= dim / 2; ++i)
                                if (dest.equals(players[i])) {
                                    check = 0;
                                    break;
                                }
                            if (check == 1)
                                validity = true;
                        }
                    } else {
                        if(dest.length() > 2 && origin.length() > 2) {
                            if (((((int) (dest.charAt(1)) - 48) * 10 + (int) (dest.charAt(2)) - 48) <= dim) && ((((((int) (dest.charAt(1)) - 48) * 10 + (int) (dest.charAt(2)) - 48) - (((int) (origin.charAt(1)) - 48) * 10 + (int) (origin.charAt(2)) - 48)) == -1) || (((((int) (dest.charAt(1)) - 48) * 10 + (int) (dest.charAt(2)) - 48) - (((int) (origin.charAt(1)) - 48) * 10 + (int) (origin.charAt(2)) - 48)) == 1))) {
                                for (i = 0; i <= dim / 2; ++i)
                                    if (dest.equals(players[i])) {
                                        check = 0;
                                        break;
                                    }
                                if (check == 1)
                                    validity = true;
                            }
                        }
                        else if(dest.length() > 2 && origin.length() == 2) {
                            if (((((int) (dest.charAt(1)) - 48) * 10 + (int) (dest.charAt(2)) - 48) <= dim) && (((((int) (dest.charAt(1)) - 48) * 10 + (int) (dest.charAt(2)) - 48) - ((int) (origin.charAt(1)) - 48) == -1) || ((((int) (dest.charAt(1)) - 48) * 10 + (int) (dest.charAt(2)) - 48) - (((int) (origin.charAt(1)) - 48)) == 1))) {
                                for (i = 0; i <= dim / 2; ++i)
                                    if (dest.equals(players[i])) {
                                        check = 0;
                                        break;
                                    }
                                if (check == 1)
                                    validity = true;
                            }
                        }
                        else if(dest.length() == 2 && origin.length() > 2) {
                            if ((((int) (dest.charAt(1)) - 48) <= dim) && (((((int) (dest.charAt(1)) - 48) - (((int) (origin.charAt(1)) - 48) * 10 + (int) (origin.charAt(2)) - 48)) == -1) || ((((int) (dest.charAt(1)) - 48) - (((int) (origin.charAt(1)) - 48) * 10 + (int) (origin.charAt(2)) - 48)) == 1))) {
                                for (i = 0; i <= dim / 2; ++i)
                                    if (dest.equals(players[i])) {
                                        check = 0;
                                        break;
                                    }
                                if (check == 1)
                                    validity = true;
                            }
                        }
                        else {
                            if ((((int) (dest.charAt(1)) - 48) <= dim) && ((((int) (dest.charAt(1)) - 48) - ((int) (origin.charAt(1)) - 48) == -1) || (((int) (dest.charAt(1)) - 48) - ((int) (origin.charAt(1)) - 48) == 1))) {
                                for (i = 0; i <= dim / 2; ++i)
                                    if (dest.equals(players[i])) {
                                        check = 0;
                                        break;
                                    }
                                if (check == 1)
                                    validity = true;
                            }
                        }
                    }
                }
            }
        } else {
            for (i = 0; i < dim / 2; ++i)
                if (origin.equals(players[i])) {
                    if ((dest.charAt(0) <= (char) (64 + dim)) && (((dest.charAt(0) - origin.charAt(0)) == 1) || ((dest.charAt(0) - origin.charAt(0)) == -1))) {
                        if (dim < 10) {
                            if (((int) (dest.charAt(1)) - 48 <= dim) && (int) (dest.charAt(1) - origin.charAt(1)) == 1)
                                for (i = 0; i <= dim / 2; ++i)
                                    if (dest.equals(players[i])) {
                                        check = 0;
                                        break;
                                    }
                        } else if (((((int) (dest.charAt(1)) - 48) * 10 + (int) (dest.charAt(2)) - 48) <= dim) && (((((int) (dest.charAt(1)) - 48) * 10 + (int) (dest.charAt(2)) - 48) - (((int) (origin.charAt(1)) - 48) * 10 + (int) (origin.charAt(2)) - 48)) == 1))
                            for (i = 0; i < dim / 2; ++i)
                                if (dest.equals(players[i])) {
                                    check = 0;
                                    break;
                                }
                        if (check == 1)
                            validity = true;
                    }
                }
        }
        return validity;
    }
}
