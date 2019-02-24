/*
Name: Dallin Drollinger
A#: A01984170

Description: Game.java includes all the functions that deal with printing and playing tile games.
    It relies on AVLSolver and QueueSolver to actually do the solving.
 */

public class Game {
    public final String difSpacing = "\t\t\t\t";

    //function prints out all the games given in a format
    public void printAllGames(int[][] allGames) {
        for (int i = 0; i < allGames.length; i++) {
            System.out.print("Game" + i + "\t\t");
        }
        System.out.println();
        for (int i = 0; i < Board.SIZE; i++) {
            for (int[] game : allGames) {
                for (int z = 0; z < Board.SIZE; z++) {
                    System.out.print(game[z + (i * Board.SIZE)] + " ");
                }
                System.out.print("\t\t");
            }
            System.out.println();
        }
    }

    //function plays the board given
    public void playGiven(String label, Board b) {
        System.out.println("\t\t\t\t" + label);
        for(int i = 0; i < Board.SIZE; i++) {
            System.out.println("\t\t\t\t\t" + b.singLine(i));
        }
        System.out.println();

        printSolution(b);
        System.out.println("\n__________________________________________________\n\n");
    }

    //function solves the given board and prints the solution
    public void printSolution(Board startBoard) {
        QueueSolver queueS = new QueueSolver();
        AVLSolver AVLS = new AVLSolver();

        //print out solution for the board iterating through each step listed unless board is unsolvable
        if (queueS.solveBoard(startBoard) || AVLS.solveBoard(startBoard)) {
            System.out.println("Puzzle is not Solvable");
        }
        else {
            Board queBoardStart = new Board(startBoard);
            Board AVLBoardStart = new Board(startBoard);
            System.out.println("Queue Solution:" + difSpacing + "AVL Tree Solution:");
            System.out.print(meshBoards(queBoardStart, AVLBoardStart));

            for (int i = 1; i < queueS.solution.item.listOfMoves.length(); i++) {
                char qMove = queueS.solution.item.listOfMoves.charAt(i);
                char aMove = AVLS.solution.listOfMoves.charAt(i);
                System.out.println(qMove + "==>" + difSpacing + "\t\t" + aMove + "==>");
                queBoardStart.makeMove(qMove, qMove);
                AVLBoardStart.makeMove(aMove, aMove);
                System.out.print(meshBoards(queBoardStart, AVLBoardStart));
            }

            System.out.println("\nMoves Required: " + queueS.solution.item.listOfMoves.length() +
                    "\t\tMoves Required: " + AVLS.solution.listOfMoves.length() +
                    "\nQueue Added: " + queueS.numberOfEnqueues +
                    "\t\tQueue Added: " + AVLS.numberOfEnqueues +
                    "\nRemoved: " + queueS.numberOfDequeues +
                    "\t\t\tRemoved: " + AVLS.numberOfDequeues +
                    "\nCurrent Size: " + (queueS.numberOfEnqueues - queueS.numberOfDequeues) +
                    "\t\tCurrent Size: " + (AVLS.numberOfEnqueues - AVLS.numberOfDequeues) +
                    "\nTime: " + queueS.exeTimeNano +
                    "\t\t\tTime: " + AVLS.exeTimeNano);
            System.out.println("\nDifference in Moves: " + Math.abs(AVLS.solution.listOfMoves.length() - queueS.solution.item.listOfMoves.length()) +
                    "\nQueue has\t" + (queueS.numberOfEnqueues - AVLS.numberOfEnqueues) + " more Enqueues" +
                    "\nQueue has\t" + (queueS.numberOfDequeues - AVLS.numberOfDequeues) + " more Dequeues" +
                    "\nQueue has\t" + ((queueS.numberOfEnqueues - queueS.numberOfDequeues) - (AVLS.numberOfEnqueues - AVLS.numberOfDequeues)) + " more Current Nodes" +
                    "\nQueue took\t" + (queueS.exeTimeNano - AVLS.exeTimeNano) + " more nano seconds");
        }
    }

    public String meshBoards(Board b1, Board b2) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < Board.SIZE; i++) {
            sb.append("\t" + b1.singLine(i) + difSpacing + "\t\t" + b2.singLine(i) + "\n");
        }

        sb.append("\n");

        return sb.toString();
    }

}
