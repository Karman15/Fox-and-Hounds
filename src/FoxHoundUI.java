import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Objects;

/**
 * A utility class for the fox hound program.
 * <p>
 * It contains helper functions for all user interface related
 * functionality such as printing menus and displaying the game board.
 */
public class FoxHoundUI {

    /**
     * Number of main menu entries.
     */
    private static final int MENU_ENTRIES = 4;
    /**
     * Main menu display string.
     */
    private static final String MAIN_MENU =
            "\n1. Move\n2. Save Game\n3. Load Game\n4. Exit\n\nEnter 1 - 4:";

    /**
     * Menu entry to select a move action.
     */
    public static final int MENU_MOVE = 1;
    /**
     * Menu entry to save the game.
     */
    public static final int MENU_SAVE = 2;
    /**
     * Menu entry to load the game.
     */
    public static final int MENU_LOAD = 3;
    /**
     * Menu entry to terminate the program.
     */
    public static final int MENU_EXIT = 4;

    private static int check;

    /**
     * Displays the game board
     *
     * @param players   array containing the positions of fox and hounds
     * @param dimension size of the board
     * @throws IllegalArgumentException if the given dimension is invalid
     * @throws NullPointerException     if player array is null
     */
    public static void displayBoard(String[] players, int dimension) {
        if (dimension < 0)
            throw new IllegalArgumentException("Error : Dimensions cannot be a negative number!");
        if (dimension < FoxHoundUtils.MIN_DIM || dimension > FoxHoundUtils.MAX_DIM)
            throw new IllegalArgumentException("Error : Dimension should be between 4 and 26(inclusive)!");
        if (players == null)
            throw new NullPointerException("Error : Array containing positions of fox and hounds cannot be empty!");
        String co_ords;
        if (dimension < 10) {
            System.out.print("  ");
            for (int i = 1; i <= dimension; ++i)
                System.out.print((char) (65 + i - 1));
            System.out.print("  \n\n");
            for (int i = 1; i <= dimension; ++i) {
                System.out.print(i + " ");
                for (char ch = 'A'; ch <= (char) (64 + dimension); ++ch) {
                    co_ords = ch + Integer.toString(i);
                    for (int j = 0; j < players.length - 1; ++j) {
                        if (players[j].equals(co_ords)) {
                            System.out.print('H');
                            check = 1;
                            break;
                        } else if (players[players.length - 1].equals(co_ords)) {
                            System.out.print('F');
                            check = 1;
                            break;
                        } else
                            check = 0;
                    }
                    if (check == 0)
                        System.out.print('.');
                }
                System.out.println(" " + i);
            }
            System.out.println();
            System.out.print("  ");
            for (int i = 1; i <= dimension; ++i)
                System.out.print((char) (65 + i - 1));
            System.out.print("  ");
        } else {
            System.out.print("   ");
            for (int i = 1; i <= dimension; ++i)
                System.out.print((char) (65 + i - 1));
            System.out.print("\n\n");
            for (int i = 1; i <= dimension; ++i) {
                if (i < 10)
                    System.out.print("0" + i + " ");
                else
                    System.out.print(i + " ");
                for (char ch = 'A'; ch <= (char) (64 + dimension); ++ch) {
                    co_ords = ch + Integer.toString(i);
                    for (int j = 0; j < players.length - 1; ++j) {
                        if (players[j].equals(co_ords)) {
                            System.out.print('H');
                            check = 1;
                            break;
                        } else if (players[players.length - 1].equals(co_ords)) {
                            System.out.print('F');
                            check = 1;
                            break;
                        } else
                            check = 0;
                    }
                    if (check == 0)
                        System.out.print('.');
                }
                if (i < 10)
                    System.out.println(" 0" + i);
                else
                    System.out.println(" " + i);
            }
            System.out.println();
            System.out.print("   ");
            for (int i = 1; i <= dimension; ++i)
                System.out.print((char) (65 + i - 1));
        }
        System.out.println("\n");
    }

    /**
     * Print the main menu and query the user for an entry selection.
     *
     * @param figureToMove the figure type that has the next move
     * @param stdin        a Scanner object to read user input from
     * @return a number representing the menu entry selected by the user
     * @throws IllegalArgumentException if the given figure type is invalid
     * @throws NullPointerException     if the given Scanner is null
     */
    public static int mainMenuQuery(char figureToMove, Scanner stdin) {
        Objects.requireNonNull(stdin, "Given Scanner must not be null");
        if (figureToMove != FoxHoundUtils.FOX_FIELD
                && figureToMove != FoxHoundUtils.HOUND_FIELD) {
            throw new IllegalArgumentException("Given figure field invalid: " + figureToMove);
        }

        String nextFigure =
                figureToMove == FoxHoundUtils.FOX_FIELD ? "Fox" : "Hounds";

        int input = -1;
        while (input == -1) {
            System.out.println(nextFigure + " to move");
            System.out.println(MAIN_MENU);

            boolean validInput = false;
            if (stdin.hasNextInt()) {
                input = stdin.nextInt();
                validInput = input > 0 && input <= MENU_ENTRIES;
            }

            if (!validInput) {
                System.out.println("Please enter valid number.");
                input = -1; // reset input variable
            }

            stdin.nextLine(); // throw away the rest of the line
        }

        return input;
    }

    /**
     * Print the main menu and query the user for an entry selection.
     *
     * @param dim     the dimension of the game board
     * @param test_in a Scanner object to read user input from
     * @return a string including the initial and final destination
     * @throws IllegalArgumentException if the given dimension is invalid
     * @throws NullPointerException     if the given Scanner is null
     */
    public static String[] positionQuery(int dim, Scanner test_in) {
        if (dim < 0)
            throw new IllegalArgumentException("Error : Dimensions cannot be a negative number!");
        if (dim < FoxHoundUtils.MIN_DIM || dim > FoxHoundUtils.MAX_DIM)
            throw new IllegalArgumentException("Error : Dimension should be between 4 and 26(inclusive)!");
        int input = -1, row_origin, row_dest;
        String coordinates;
        char column = (char) (64 + dim);
        String[] coords = new String[2];
        // while loop runs till the user inputs valid co-ordinates
        while (input == -1) {
            System.out.println("Provide origin and destination coordinates.\nEnter two positions between A1-" + column + dim + ":");
            coordinates = test_in.nextLine();
            Objects.requireNonNull(test_in, "Given Scanner must not be null");
            coords = coordinates.split(" ");
            if (coords.length != 2)
                System.err.println("Error: Invalid co-ordinates!");
            else {
                if (dim < 10) {
                    row_origin = coords[0].charAt(1) - 48;
                    row_dest = coords[1].charAt(1) - 48;
                } else {
                    if(coords[0].length() > 2 && coords[1].length() > 2) {
                        row_origin = (coords[0].charAt(1) - 48) * 10 + coords[0].charAt(2) - 48;
                        row_dest = (coords[1].charAt(1) - 48) * 10 + coords[1].charAt(2) - 48;
                    }
                    else if(coords[1].length() > 2 && coords[0].length() == 2) {
                        row_origin = coords[0].charAt(1) - 48;
                        row_dest = (coords[1].charAt(1) - 48) * 10 + coords[1].charAt(2) - 48;
                    }
                    else if(coords[1].length() == 2 && coords[0].length() > 2) {
                        row_origin = (coords[0].charAt(1) - 48) * 10 + coords[0].charAt(2) - 48;
                        row_dest = coords[1].charAt(1) - 48;
                    }
                    else {
                        row_origin = coords[0].charAt(1) - 48;
                        row_dest = coords[1].charAt(1) - 48;
                    }
                }
                if (65 <= (int) coords[0].charAt(0) && (int) coords[0].charAt(0) <= (int) column && 65 <= (int) coords[1].charAt(0) && (int) coords[1].charAt(0) <= (int) column && 1 <= row_origin && 1 <= row_dest && row_origin <= dim && row_dest <= dim)
                    input = 1;
                else {
                    System.out.println();
                    System.err.println("ERROR: Please enter valid coordinate pair separated by space.");
                }
            }
        }
        String[] positions = new String[coords.length];
        positions[0] = coords[0];
        positions[1] = coords[1];
        return positions;
    }

    /**
     * Print the main menu and query the user for an entry selection.
     *
     * @param test_in a Scanner object to read user input from
     * @return a path to the file where the game is loaded from or saved
     */
    public static Path fileQuery(Scanner test_in) {
        System.out.print("Enter file path: ");
        return Paths.get(test_in.next());
    }
}







