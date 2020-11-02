import java.io.*;
import java.util.Random;

public class demo {
    public static void main(String[] args) {
        while (true) {
            try {
                Random random = new Random();
                int text = random.nextInt(2) + 1;
                System.out.println(text);
                FileWriter fw = new FileWriter("game.txt");
                BufferedWriter out = new BufferedWriter(fw);
                out.write(text + "");
                out.close();
                Thread.sleep(2000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
