/*
Name: Dallin Drollinger
A#: A01984170

Description: SliderGame.java houses our main function to play tile games and focuses mainly on user input.
                It utilizes the Board and Game classes to track and solve the boards which the user selects.
 */
import java.util.Scanner;

public class SliderGame {
    //function for checking user input to make sure that it is of type int
    //this will repeatedly ask the user for an int until they enter one
    static private int badInputCheckerForInt() {
        Scanner in = new Scanner(System.in);
        do {
            try {
                return in.nextInt();
            }
            catch (java.util.InputMismatchException ex) {
                System.out.println("\nYou need to enter a NUMBER!");
                System.out.print("Please try again: ");
                in.nextLine();
            }
        } while (true);
    }

    //main function
    public static void main(String[] args) {
        //creating game and declaring variables
        Game g = new Game();
        int numberEntered;
        boolean wantsToPlayAgain = true;

        int[][] allGames = {{4, 0, 1, 3, 5, 2, 6, 8, 7},    //game0
                {1, 3, 8, 6, 2, 0, 5, 4, 7},                //game1
                {1, 3, 2, 4, 5, 6, 8, 7, 0},                //game2
                {1, 2, 3, 7, 4, 0, 6, 5, 8}};               //game3

        //plays game until user stops loop
        System.out.println();
        System.out.println("     Welcome to Game Solver Compare!\n");
        while(wantsToPlayAgain) {


            g.printAllGames(allGames);
            System.out.println();

            System.out.print("Would you like to solve all boards(0) or solve a specific board(1): ");
            numberEntered = SliderGame.badInputCheckerForInt();
            System.out.println();

            //outcome is selected based on users choice
            if (numberEntered == 0) {
                for(int[] game: allGames) {
                    Board b = new Board();
                    b.makeBoard(game);
                    g.playGiven("Chosen Board:", b);
                }
            }
            else if (numberEntered == 1) {
                System.out.print("\nWhich game would you like to play(0-" + Integer.toString(allGames.length - 1) + "): ");
                numberEntered = SliderGame.badInputCheckerForInt();
                while(numberEntered > allGames.length - 1 || numberEntered < 0) {
                    System.out.print("Sorry that board number is not available. Please try again: ");
                    numberEntered = SliderGame.badInputCheckerForInt();
                }
                System.out.println();

                Board b = new Board();
                b.makeBoard(allGames[numberEntered]);
                g.playGiven("Chosen Board:", b);
            }
            else {
                System.out.println("Please enter a valid number(0 or 1)\n");
            }
            //user prompted for playing again
            System.out.print("\nWould you like to play again? Yes(1) or No(0): ");
            numberEntered = SliderGame.badInputCheckerForInt();
            if(numberEntered != 0) {
                if(numberEntered != 1) {
                    System.out.println("\nThat is not a valid option\nYou will now be ejected from the game");
                }
                System.out.println();
            }
            else {
                wantsToPlayAgain = false;
            }
        }
    }
}
