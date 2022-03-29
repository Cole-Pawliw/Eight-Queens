import java.util.Arrays;

/**
 * @author Cole Pawliw <a href="mailto:cole.pawliw@ucalgary.ca">cole.pawliw@ucalgary.ca</a>
 * @version 1.2
 * @since 1.0
 */

public class EightQueens implements Cloneable {
    private char[][] board = new char[8][8];
    private char[][] safe = new char[8][8];
    private int currentQueens;

    /**
     * Fills the char[][] arrays with o
     */
    public EightQueens() {
        for (int i = 0; i < 8; i++) {
            Arrays.fill(board[i], 'o');
            Arrays.fill(safe[i], 'o');
        }

        currentQueens = 0;
    }

    /**
     * Makes and returns and identical copy of the current object
     * @return An EightQueens object identical to this
     * @throws CloneNotSupportedException throws when the object cannot be cloned
     */
    public Object clone() throws CloneNotSupportedException {
        EightQueens copy = (EightQueens)super.clone();

        copy.board = new char[8][8];
        copy.safe = new char[8][8];
        for (int i = 0; i < 8; i++) {
            copy.board[i] = Arrays.copyOf(this.board[i], 8);
            copy.safe[i] = Arrays.copyOf(this.safe[i], 8);
        }
        return copy;
    }

    /**
     * Returns the board
     * @return a 2D char array
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Sets the space at the indicated coordinates to have a queen
     * @param row an int from 0-7 specifying what row to place the queen
     * @param column an int from 0-7 specifying what column to place the queen
     */
    public void setQueen(int row, int column) {
        if (board[row][column] != 'Q') { //Only tries to place the queen if the space is empty
            board[row][column] = 'Q';
            currentQueens++;
        }
    }

    /**
     * Sets the space at the indicated coordinates to be empty
     * @param row an int from 0-7 specifying what row to make empty
     * @param column an int from 0-7 specifying what column to make empty
     */
    public void emptySquare(int row, int column) {
        if (board[row][column] == 'Q') { //Only tries to empty the space if there is a queen
            board[row][column] = 'o';
            currentQueens--;
        }
    }

    /**
     * Calls on other functions to find unsafe spaces and place additional queens
     * Gets other functions to do its dirty work
     * @param queensRemaining An int from 1-8 specifying how many queens to place on the board
     * @return Returns true if there is a way to place queensRemaining queens, false otherwise
     */
    public boolean setQueens(int queensRemaining) {
        boolean success;

        if (queensRemaining < 1 || queensRemaining > 8) {
            throw new IllegalArgumentException("Cannot place more than 8 or less than 1 queen");
        }

        success = markSafe();
        if (!success) {
            System.out.println("Starting board has queens attacking queens.");
            return false; //Returns if the current board has invalid queens
        }

        success = place(queensRemaining); //Attempts to place all the queens
        if (!success) {
            System.out.println(queensRemaining + " Queens could not be placed.");
        }
        return success;
    }

    /**
     * Finds any coordinates in board that contains a queen, sends those coordinates to unsafeMarker
     * @return Returns true if the current queens have a valid placement, false otherwise
     */
    private boolean markSafe() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) { //Checks every coordinate set for a queen
                if (board[i][j] == 'Q') {
                    if (!unsafeMarker(i, j)) { //Checks location to place queen and marks appropriate spaces unsafe
                        return false;
                    }
                }

            }
        }
        return true;
    }

    /**
     * Checks given coordinates for a safe spot. If safe, places queen and marks any spots attacked by that queen
     * @param row an int from 0-7 specifying what row the queen is in
     * @param column an int from 0-7 specifying what column the queen is in
     * @return Returns true if the current queen is in a safe spot, false otherwise
     */
    private boolean unsafeMarker(int row, int column) {
        if (safe[row][column] != 'o') { //Checks if current queen is already unsafe
            return false; //Unsafe means the board already does not work
        } else {
            safe[row][column] = 'Q'; //Marks the safe space as a queen
        }

        //Marking any appropriate spaces as unsafe by using an increasing radius of i
        for (int i = 1; i < 8; i++) {
            if (row + i < 8) { //Any spaces to the right of the queen (including diagonals)
                safe[row+i][column] = 'x'; //Right

                if (column + i < 8) {
                    safe[row+i][column+i] = 'x'; //Down and right
                }
                if (column - i >= 0) {
                    safe[row+i][column-i] = 'x'; //Up and right
                }
            }

            if (row - i >= 0) { //Any spaces to the left of the queen (including diagonals)
                safe[row-i][column] = 'x'; //Left

                if (column + i < 8) {
                    safe[row-i][column+i] = 'x'; //Down and left
                }
                if (column - i >= 0) {
                    safe[row-i][column-i] = 'x'; //Up and left
                }
            }

            if (column + i < 8) { //Any spaces below the queen
                safe[row][column+i] = 'x'; //Down
            }

            if (column - i >= 0) { //Any spaces above the queen
                safe[row][column-i] = 'x'; //Up
            }
        }
        return true;
    }

    /**
     * Recursively places queens in safe spots to try and place the specified number of queens
     * @param number The number of queens to attempt to place
     * @return Returns true if it can find a way to place all the required queens, false otherwise
     */
    private boolean place(int number) {
        char[][] storage = new char[8][8];
        boolean placed;

        if (number < 1) { //Doesn't have to place any pieces, just returns
            return true;
        }

        for (int k = 0; k < 8; k++) {
            storage[k] = Arrays.copyOf(safe[k], 8); //Stores the safe array so it can be changed back whenever
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) { //Checks every coordinate set for an empty space
                if (storage[i][j] == 'o') {
                    unsafeMarker(i, j); //Places queen in the found safe space
                    if (number == 1) {
                        board[i][j] = 'Q'; //The found space works, marks it in the official board
                        return true;
                    }

                    placed = place(number - 1); //Recursive call, tests 1 less queen
                    if (placed) {
                        board[i][j] = 'Q'; //The found space works, marks it in the official board
                        return true;
                    } else {
                        storage[i][j] = 'x'; //The current space cannot contain a queen
                        for (int k = 0; k < 8; k++) {
                            safe[k] = Arrays.copyOf(storage[k], 8); //Resets the safe array to start over
                        }

                    }
                }
            }
        }

        //If the for statement breaks, the placement failed
        return false;
    }
}
