/*
Name: Dallin Drollinger
A#: A01984170

Description: AVLSolver is based on QueueSolver and will add boards to an AVL tree comparing on manhattan
    distances. See Board.java for manDistance function.
 */
import java.util.HashSet;
import java.util.Set;

public class AVLSolver {

    //Variables for tracking data
    public Board solution;
    long exeTimeNano;
    int numberOfDequeues;
    int numberOfEnqueues;

    //Constructor
    public AVLSolver() {
        this.solution = null;
        this.exeTimeNano = 0;
        this.numberOfDequeues = 0;
        this.numberOfEnqueues = 0;
    }

    //function solves the given board and prints the solution
    public boolean solveBoard(Board startBoard) {
        //makes a AVL tree and puts the starting board in it
        AVLTree<Board> tree = new AVLTree<>();
        tree.insert(startBoard);
        Board curBoard = new Board();

        //creating needed variables
        char[] moveOptions = {'R', 'D', 'L', 'U'};
        boolean boardIsSolved = false;
        boolean puzzleIsUnsolvable = false;
        long startTime;
        long endTime;
        Set<Integer> boardIDList = new HashSet<>();

        //while the board is not solved, we remove a board and insert all possible moves from it
        //if we have already seen a board before, we don't add it onto the tree
        startTime = System.nanoTime();
        do {
            curBoard = tree.findMin();
            tree.deleteMin();  //delete the node we just took out
            this.numberOfDequeues += 1;
            for (char move : moveOptions) {
                Board temp = new Board(curBoard);
                if (move == temp.makeMove(move, curBoard.listOfMoves.charAt(curBoard.listOfMoves.length() - 1))) {
                    if (!boardIDList.contains(temp.boardID())) {
                        if (temp.isSolved()) {
                            curBoard.listOfMoves += move;
                            boardIsSolved = true;
                        }
                        else {
                            temp.listOfMoves = curBoard.listOfMoves + move;
                            tree.insert(temp);
                            boardIDList.add(temp.boardID());
                            this.numberOfEnqueues += 1;
                        }
                    }
                }
            }
            //check for empty tree, meaning puzzle is unsolvable
            if (this.numberOfEnqueues - this.numberOfDequeues < 1) {
                puzzleIsUnsolvable = true;
            }
        } while (!boardIsSolved && !puzzleIsUnsolvable);
        endTime = System.nanoTime();

        this.solution = curBoard;
        this.exeTimeNano = endTime - startTime;

        return puzzleIsUnsolvable;
    }
}
