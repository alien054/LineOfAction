import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class demo {
    public static void test(Player player)
    {
        player.readFile("init2.txt");
        ArrayList<Position> possibleMoves = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        while (true)
        {
//            System.out.print("x: ");
//            int x1 = input.nextInt();
//            System.out.print("y: ");
//            int y1 = input.nextInt();
//            Position p1 = new Position(x1,y1);

            System.out.println(player.quadEVAL(player.board,player.getColor()));
            break;

//            System.out.print("x: ");
//            int x2 = input.nextInt();
//            System.out.print("y: ");
//            int y2 = input.nextInt();
//            Position p2 = new Position(x2,y2);

//            possibleMoves = player.getAvailableMoves(new Position(x,y));
//            for(Position p: possibleMoves)
//            {
//                System.out.println("Moves: " + p);
//            }

//            System.out.println("Is connected: " + p1.isConnected(p2));

        }
    }

    public static void test2(Player player)
    {
        player.readFile("init.txt");
        player.densityEVAL(player.board,player.getColor());
    }

    public static void loopTest(Player player)
    {
        String path = "game.txt";
        Board move = new Board(player.boardSize);
        double timeout = (player.boardSize == 8) ? 2000 : 1000;
        while (true)
        {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(player.readFile(path))
            {
                //Check the game status --> 1 or 2 means game is still alive
                // otherwise game is over.
                if(player.getTurn() == player.getColor())
                {
                    System.out.println("From file: ");
                    player.printBoard(player.board);

                    move = player.findBestMove(player.board,timeout);

                    // If after making the move any of the player won
                    // Current player send the result to server and resigns
                    if(player.isGameOver(move,player.getColor()))
                    {
                        String color = player.getColor() == 1 ? "White" : "Black";
                        System.out.println(color + " (You)  win");
                        player.writeFile(path,100,move);     // 100 means game win by current player
                        break;
                    }
                    else if(player.isGameOver(move,player.getOpponentColor()))
                    {
                        String color = player.getOpponentColor() == 1 ? "White" : "Black";
                        System.out.println(color + " (Opponent)  win");
                        player.writeFile(path,200,move);     // 200 means game win by opponent player
                        break;
                    }
                    else player.writeFile(path,0,move);     // 0 means server's turn
                }
                //If player receives 100 or 200 from server
                //Its means game is already over.
                else if(player.getTurn() == 100)    // Opponent has declared itself as winner
                {
                    String color = player.getOpponentColor() == 1 ? "White" : "Black";
                    System.out.println(color + "(opponent) wins");
                    break;
                }
                else if(player.getTurn() == 200)    // Opponent has declared this player as winner
                {
                    String color = player.getColor() == 1 ? "White" : "Black";
                    System.out.println(color + "(You) wins");
                    break;
                }
            }
            else
            {
                System.out.println("NO FILE");
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Select Color: 1.White 2.Black");
        int choice = input.nextInt();

        Player player = new Player(choice);

        System.out.println("hur choice 1. quad .25 2. quad .5");
        choice = input.nextInt();
        player.hurChoice = choice;

//        demo.test(player);
        demo.loopTest(player);
//        demo.test2(player);
    }

}
