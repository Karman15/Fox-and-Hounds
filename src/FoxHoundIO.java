import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * A utility class for the fox hound program.
 * <p>
 * It contains helper functions for all file input / output
 * related operations such as saving and loading a game.
 */
public class FoxHoundIO {

    /**
     * Loads the game if file is found
     *
     * @param players an array containing the positions of fox and hounds
     * @param input   a path to the file to be loaded
     * @return '#' if the file fails to load
     * @throws IllegalArgumentException if the given player array is invalid
     * @throws NullPointerException     if the given path or player array is null
     */
    public static char loadGame(String[] players, Path input) {
        if (input == null)
            throw new NullPointerException("Error: File name cannot be empty!");
        if (players == null)
            throw new NullPointerException("Error: Array containing positions of fox and hounds cannot be empty!");
        if (players.length != 5) {
            throw new IllegalArgumentException("Error: Length of array containing positions of fox and hounds has to be 5!");
        }
        try {
            String file_name = input.toString(); // converts the file name into a string
            File myObj = new File(file_name); // file object being created
            Scanner myReader = new Scanner(myObj);
            int counter = 0;
            // while loop to check if the file contains a single line
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                ++counter;
            }
            myReader = new Scanner(myObj);
            if (counter == 1) {
                String data = myReader.nextLine();
                String[] move_positions = data.split(" ");
                if ((!move_positions[0].equals("F") && !move_positions[0].equals("H")) || move_positions.length != 6)
                    return '#';
                for (int i = 1; i < move_positions.length; ++i) {
                    if (move_positions[i].charAt(0) < 65 || move_positions[i].charAt(0) > 64 + FoxHoundUtils.DEFAULT_DIM || move_positions[i].charAt(1) - 48 < 1 || move_positions[i].charAt(1) - 48 > FoxHoundUtils.DEFAULT_DIM)
                        return '#';
                }
                char figure = move_positions[0].charAt(0);
                for (int i = 0; i < players.length; ++i) {
                    players[i] = move_positions[i + 1];
                }
                return figure;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
            return '#';
        } catch (NullPointerException e) {
            System.err.println("Error: Null pointer exception!");
            return '#';
        }
        return '#';
    }

    /**
     * Saves the game to the file
     *
     * @param players  an array containing the positions of fox and hounds
     * @param nextMove a character representing the piece(fox or hounds) to be moved in the next turn
     * @param saveFile a path to the file where the game is to be saved
     * @return a boolean value indicating whether the game has been saved or not
     * @throws IllegalArgumentException if the given player array or figure to be moved is invalid
     * @throws NullPointerException     if the given path or player array is null
     */
    public static boolean saveGame(String[] players, char nextMove, Path saveFile) {
        if (players == null) {
            throw new NullPointerException("Error: Array containing positions of fox and hounds cannot be empty!");
        }
        if (saveFile == null)
            throw new NullPointerException("Error: File name cannot be empty!");
        if (players.length != 5) {
            throw new IllegalArgumentException("Error: Length of players array has to be 5!");
        }
        if ((nextMove != FoxHoundUtils.FOX_FIELD && nextMove != FoxHoundUtils.HOUND_FIELD)) {
            throw new IllegalArgumentException("Error: Next piece to move can either be 'F' or 'H'!");
        }
        String file_name = saveFile.toString();
        try {
            File myObj = new File(file_name);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.err.println("File already exists!");
            }
            FileWriter myWriter = new FileWriter(file_name);
            String positions = String.join(" ", players);
            String content = nextMove + " " + positions;
            myWriter.write(content);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.err.println("Error: IO Exception!");
            return false;
        } catch (NullPointerException e) {
            System.err.println("Error: Saving failed due to null pointer!");
            return false;
        }
        return true;
    }
}
