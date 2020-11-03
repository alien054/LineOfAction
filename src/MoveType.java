public class MoveType
{
    public String type;
    public int pieceCount;

    MoveType(String type,int pieceCount)
    {
        this.type = type;
        this.pieceCount = pieceCount;
    }

    @Override
    public String toString() {
        return  type + "\tPieces: " + pieceCount;
    }
}
