import java.util.Arrays;

public class Test
{
    public static void main(String[] args) {
        int [][] b1 = {{1,2,3},{4,5,6},{7,8,9}};
        int [][] b2 = {{1,1,1},{4,5,6},{7,8,9}};
        int [][] b3 = {{1,2,3},{4,5,6},{7,8,9}};

        Board n1 = new Board(b1);
        Board n2 = new Board(b2);
        Board n3 = new Board(b3);

        System.out.println(Arrays.deepEquals(b1,b2));
        System.out.println(Arrays.deepEquals(b1,b3));
        System.out.println(n1.equals(n2));
        System.out.println(n1.equals(n3));
    }
}
