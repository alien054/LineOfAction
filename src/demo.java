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
            System.out.print("x: ");
            int x1 = input.nextInt();
            System.out.print("y: ");
            int y1 = input.nextInt();
            Position p1 = new Position(x1,y1);

            System.out.print("x: ");
            int x2 = input.nextInt();
            System.out.print("y: ");
            int y2 = input.nextInt();
            Position p2 = new Position(x2,y2);

//            possibleMoves = player.getAvailableMoves(new Position(x,y));
//            for(Position p: possibleMoves)
//            {
//                System.out.println("Moves: " + p);
//            }

            System.out.println("Is connected: " + p1.isConnected(p2));

        }
    }

    public static void loopTest(Player player)
    {
        while (true)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            player.readFile("game.txt");
            if(player.getTurn() == player.getColor())
            {
                player.printBoard();
                player.move();

                if(player.isGameOver())
                {
                    String color = player.getColor() == 1 ? "White" : "Black";
                    System.out.println(color + " (You)  win");
                    player.writeFile("game.txt",100);     // 100 means game over
                    break;
                }
                else player.writeFile("game.txt",0);     // 0 means server
            }
            else if(player.getTurn() == 100)
            {
                String color = player.getOpponentColor() == 1 ? "White" : "Black";
                System.out.println(color + "(opponent) wins");
                break;
            }
        }
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Select Color: 1.White 2.Black");
        int choice = input.nextInt();

        Player player = new Player(choice);

//        demo.test(player);
        demo.loopTest(player);
    }
}
