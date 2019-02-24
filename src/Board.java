/*
Name: Dallin Drollinger
A#: A01984170

Description: Board.java was imported into this project and I am not the original author of most of the functions
                This class allows us to create board objects which we can use in our queue
 */
import java.util.Random;

public class Board implements Comparable<Board> {
    public static final int SIZE = 3;
    private int[][] board;  // Values of board
    private int blankRow;   // Row location of blank
    private int blankCol;   // Column location of blank
    public String listOfMoves = "";

    public Board() {
        board = new int[SIZE][SIZE];
    }

    //Copy constructor
    Board(Board b) {
        board = new int[SIZE][];
        for (int i = 0; i < SIZE; i++)
            this.board[i] = b.board[i].clone();
        this.blankRow = b.blankRow;
        this.blankCol = b.blankCol;
    }

    //function to print out board
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(board[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    //function to only print a single line from the board. If line
    public String singLine(int line) {
        if(line >= SIZE) {
            return "ERROR: INDEX OUT OF BOUNDS";
        }

        StringBuilder sb = new StringBuilder();


        for (int col = 0; col < SIZE; col++) {
            sb.append(board[line][col]);
            sb.append(" ");
        }

        return sb.toString();
    }

    //compareTo function based on Manhattan distance
    public int compareTo(Board otherBoard) {
        return manDistance() - otherBoard.manDistance();
    }

    //Computes Manhattan distance iterating over each element on the board
    private int manDistance() {
        int totalDis = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != 0) {
                    totalDis += Math.abs(col - ((this.board[row][col] - 1) % SIZE)); //horizontal distance
                    totalDis += Math.abs(row - ((this.board[row][col] - 1) / SIZE)); //vertical distance
                    totalDis += this.listOfMoves.length();
                }
            }
        }
        return totalDis;
    }

    //function to assign a unique id to a board in the form of a number
    public int boardID() {
        int idNumber = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                idNumber += board[i][j] * Math.pow(10, (i * SIZE) + j);
            }
        }
        return idNumber;
    }

    //compares board to a solved board and returns false if there are differences
    public boolean isSolved() {
        int[][] b = {{1, 2, 3},
                     {4, 5, 6},
                     {7, 8, 0}};
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != b[i][j]) {
                    return false;
                }
            }
        }
        return true;

    }

    //Create a board by performing legal moves on a board
    //jumbleCt indicates the number of moves to make
    //if jumbleCt ==0, return the winning board
    void makeBoard(int jumbleCt) {
        int val = 1;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = val++;
        blankRow = SIZE - 1;
        blankCol = SIZE - 1;
        board[blankRow][blankCol] = 0;
        jumble(jumbleCt);
    }

    //Create a board from a given set of values
    void makeBoard(int[] values) {
        int c = 0;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                if (values[c] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
                board[i][j] = values[c++];
            }
    }

    // Randomly apply ct moves to the board, making sure they are legal and don'node undo the previous move
    void jumble(int ct) {
        Random rand = new Random();
        String moveStr = "UDLR";  // Moves representing Up, Down, Left, Right
        char lastMove = ' ';
        char thisMove = ' ';
        for (int i = 0; i < ct; i++) {
            thisMove = ' ';
            while (thisMove == ' ') {
                thisMove = moveStr.charAt(rand.nextInt(4));
                thisMove = makeMove(thisMove, lastMove);
            }
            lastMove = thisMove;
        }
    }

    // Make the move indicated by m (L Left, R Right, U Up, D Down) if it is legal and if it doesn'node undo the move specified by lastmove
    // Return a blank if the move could not be made, otherwise return the move
    char makeMove(char m, char lastmove) {
        boolean moved = false;
        switch (m) {
            case 'R':
                if (lastmove != 'L') {
                    moved = slideRight();
                }
                break;
            case 'L':
                if (lastmove != 'R') {
                    moved = slideLeft();
                }
                break;
            case 'D':
                if (lastmove != 'U') {
                    moved = slideDown();
                }
                break;
            case 'U':
                if (lastmove != 'D') {
                    moved = slideUp();
                }
                break;
        }
        if (!moved)
            return ' ';
        return m;
    }

    // If possible, slides a tile up into the blank position.  Returns success of operation.
    private boolean slideUp() {
        if (blankRow == SIZE - 1) return false;
        board[blankRow][blankCol] = board[blankRow + 1][blankCol];
        board[blankRow + 1][blankCol] = 0;
        blankRow += 1;
        return true;
    }

    // If possible, slides a tile down into the blank position.  Returns success of operation.
    private boolean slideDown() {
        if (blankRow == 0) return false;
        board[blankRow][blankCol] = board[blankRow - 1][blankCol];
        board[blankRow - 1][blankCol] = 0;
        blankRow -= 1;
        return true;
    }

    // If possible, slides a tile left into the blank position.  Returns success of operation.
    private boolean slideLeft() {
        if (blankCol == SIZE - 1) return false;
        board[blankRow][blankCol] = board[blankRow][blankCol + 1];
        board[blankRow][blankCol + 1] = 0;
        blankCol += 1;
        return true;
    }

    // If possible, slides a tile right into the blank position.  Returns success of operation.
    private boolean slideRight() {
        if (blankCol == 0) return false;
        board[blankRow][blankCol] = board[blankRow][blankCol - 1];
        board[blankRow][blankCol - 1] = 0;
        blankCol -= 1;
        return true;
    }
}
