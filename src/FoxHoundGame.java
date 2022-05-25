import java.util.Scanner;

/**
 * The Main class of the fox hound program.
 * <p>
 * It contains the main game loop where main menu interactions
 * are processed and handler functions are called.
 */
public class FoxHoundGame {

    /**
     * This scanner can be used by the program to read from
     * the standard input.
     * <p>
     * Every scanner should be closed after its use, however, if you do
     * that for StdIn, it will close the underlying input stream as well
     * which makes it difficult to read from StdIn again later during the
     * program.
     * <p>
     * Therefore, it is advisable to create only one Scanner for StdIn
     * over the course of a program and only close it when the program
     * exits. Additionally, it reduces complexity.
     */
    private static final Scanner STDIN_SCAN = new Scanner(System.in);

    /**
     * Swap between fox and hounds to determine the next
     * figure to move.
     *
     * @param currentTurn last figure to be moved
     * @return next figure to be moved
     */
    private static char swapPlayers(char currentTurn) {
        if (currentTurn == FoxHoundUtils.FOX_FIELD) {
            return FoxHoundUtils.HOUND_FIELD;
        } else {
            return FoxHoundUtils.FOX_FIELD;
        }
    }

    /**
     * The main loop of the game. Interactions with the main
     * menu are interpreted and executed here.
     *
     * @param dim     the dimension of the game board
     * @param players current position of all figures on the board in board coordinates
     */
    private static void gameLoop(int dim, String[] players) {

        // start each game with the Fox
        char turn = FoxHoundUtils.FOX_FIELD;
        boolean exit = false, validity = false;
        while (!exit) {
            System.out.println("\n#################################");
            try {
                FoxHoundUI.displayBoard(players, dim);
            } catch (IllegalArgumentException e) {
                System.err.println("Error: Illegal argument exception!");
            }
            if (FoxHoundUtils.isHoundWin(players, dim)) { // checks if hounds win
                System.out.println("The Hounds win!");
                System.exit(0);
            } else if (FoxHoundUtils.isFoxWin(players[dim / 2])) { // checks if fox win
                System.out.println("The Fox wins!");
                System.exit(0);
            }
            int choice = FoxHoundUI.mainMenuQuery(turn, STDIN_SCAN);

            // handle menu choice
            switch (choice) {
                case FoxHoundUI.MENU_MOVE: // case 1 to move a piece
                    String[] positions = FoxHoundUI.positionQuery(dim, STDIN_SCAN);
                    // while loop to check if the co-ordinates returned from position query function are valid
                    while (!validity) {
                        try {
                            validity = FoxHoundUtils.isValidMove(dim, players, turn, positions[0], positions[1]);
                        } catch (NullPointerException error) {
                            validity = FoxHoundUtils.isValidMove(dim, players, turn, positions[0], positions[1]);
                        }
                        if (!validity) {
                            System.out.println("Input valid coordinates!");
                            positions = FoxHoundUI.positionQuery(dim, STDIN_SCAN);
                        }
                    }
                    if (turn == 'F')
                        players[dim / 2] = positions[1];
                    else {
                        for (int i = 0; i < dim / 2; ++i)
                            if (players[i].equals(positions[0])) {
                                players[i] = positions[1];
                                break;
                            }
                    }
                    turn = swapPlayers(turn);
                    break;
                case FoxHoundUI.MENU_SAVE: // case 2 to save the game
                    boolean check;
                    check = FoxHoundIO.saveGame(players, turn, FoxHoundUI.fileQuery(STDIN_SCAN));
                    if (!check) // checks if the game can be saved
                        System.err.println("Error: Saving file failed.");
                    break;
                case FoxHoundUI.MENU_LOAD: // case 3 to load a game
                    char figure;
                    figure = FoxHoundIO.loadGame(players, FoxHoundUI.fileQuery(STDIN_SCAN));
                    if (figure == '#') // checks if the game can be loaded into the system
                        System.err.println("ERROR: Loading from file failed.");
                    else {
                        System.out.println("Game has been successfully loaded!");
                        turn = figure;
                    }
                    break;
                case FoxHoundUI.MENU_EXIT: // case 4 to exit out of the game
                    exit = true;
                    break;
                default: // default case in case the inputted number is not 1-4
                    System.err.println("ERROR: invalid menu choice: " + choice);
            }
        }
    }

    /**
     * Entry method for the Fox and Hound game.
     * <p>
     * The dimensions of the game board can be passed in as
     * optional command line argument.
     * <p>
     * If no argument is passed, a default dimension of
     * {@value FoxHoundUtils#DEFAULT_DIM} is used.
     * <p>
     * Dimensions must be between {@value FoxHoundUtils#MIN_DIM} and
     * {@value FoxHoundUtils#MAX_DIM}.
     *
     * @param args contain the command line arguments where the first can be
     *             board dimensions.
     */

    public static void main(String[] args) {

        int dimension = FoxHoundUtils.DEFAULT_DIM;
        String[] players;
        System.out.print("Do you wish to enter a dimension (y/y for yes, any other character for no) ? ");
        // prompts the user to enter a dimension if the given input is 'y' or 'Y'
        char choice = STDIN_SCAN.next().charAt(0);
        if (choice == 'y' || choice == 'Y') {
            System.out.print("Enter a dimension (4-26) : ");
            dimension = STDIN_SCAN.nextInt();
            int check = 0;
            // if-else block to check if inputted dimension is valid
            if (dimension < 0)
                System.out.println("Error : Dimensions cannot be a negative number!");
            else if (dimension < 4 || dimension > 26)
                System.out.println("Error : Dimension should be between 4 and 26(inclusive)!");
            else
                check = 1;
            if (check == 0)
                dimension = 8;
        }
        try {
            players = FoxHoundUtils.initialisePositions(dimension); // initialises fox and hound positions
        } catch (IllegalArgumentException error) {
            players = FoxHoundUtils.initialisePositions(FoxHoundUtils.DEFAULT_DIM);
        }
        gameLoop(dimension, players);
        // Close the scanner reading the standard input stream       
        STDIN_SCAN.close();
    }
}
