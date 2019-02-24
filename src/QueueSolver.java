/*
Name: Dallin Drollinger
A#: A01984170

Description: Game.java includes all the functions that deal with printing, playing, and solving tile games
 */
import java.util.HashSet;
import java.util.Set;

public class QueueSolver {

    public Queue.Node solution;
    long exeTimeNano;
    int numberOfDequeues;
    int numberOfEnqueues;

    public QueueSolver() {
        this.solution = null;
        this.exeTimeNano = 0;
        this.numberOfDequeues = 0;
        this.numberOfEnqueues = 0;
    }

    //function solves the given board and prints the solution
    public boolean solveBoard(Board startBoard) {
        //makes a queue and puts the starting board in it
        Queue queue = new Queue();
        queue.enqueue(startBoard);
        Queue.Node boardNode;

        //creating needed variables
        char[] moveOptions = {'R', 'D', 'L', 'U'};
        char finalMove = 'X';
        boolean boardIsSolved = false;
        boolean puzzleIsUnsolvable = false;
        long startTime;
        long endTime;
        Set<Integer> boardIDList = new HashSet<>();

        //while the board is not solved, we dequeue a board and enqueue all possible moves from it
        //if we have already seen a board before, we don't add it onto the queue
        startTime = System.nanoTime();
        do {
            boardNode = queue.dequeue();
            this.numberOfDequeues += 1;
            for (char move : moveOptions) {
                Board temp = new Board(boardNode.item);
                if (move == temp.makeMove(move, boardNode.item.listOfMoves.charAt(boardNode.item.listOfMoves.length() - 1))) {
                    if (!boardIDList.contains(temp.boardID())) {
                        if (temp.isSolved()) {
                            boardNode.item.listOfMoves += move;
                            boardIsSolved = true;
                        }
                        else {
                            queue.enqueue(temp, boardNode.item.listOfMoves + move);
                            boardIDList.add(temp.boardID());
                            this.numberOfEnqueues += 1;
                        }
                    }
                }
            }
            //check for empty queue, meaning puzzle is unsolvable
            if (this.numberOfEnqueues - this.numberOfDequeues < 1) {
                puzzleIsUnsolvable = true;
            }
        } while (!boardIsSolved && !puzzleIsUnsolvable);
        endTime = System.nanoTime();

        this.solution = boardNode;
        this.exeTimeNano = endTime - startTime;

        return puzzleIsUnsolvable;
    }
}
