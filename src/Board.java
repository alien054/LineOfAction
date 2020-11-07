import java.util.Arrays;

public class Board
{
    public int [][] board_;

    Board(int boardSize)
    {
        board_ = new int[boardSize][boardSize];
    }

    Board(int[][] board_)
    {
        this.board_ = board_;
    }

    public int[][] getBoard_()
    {
        return board_;
    }

    public void copy(Board other)
    {
        int dim = other.getBoard_().length;
        for(int i=0;i<dim;i++)
        {
            for (int j=0;j<dim;j++)
            {
                this.board_[i][j] = other.getBoard_()[i][j];
            }
        }
    }


    @Override
    public boolean equals(Object obj) {
        String cur_str;
        String other_str;

        Board other = (Board) obj;
        cur_str = Arrays.deepToString(this.board_);
        other_str = Arrays.deepToString(other.board_);

        //System.out.println("cur: " + cur_str);
        //System.out.println("other: " + other_str);

        return cur_str.equalsIgnoreCase(other_str);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board_);
    }
}
