public class Position
{
    public int x;
    public int y;

    Position(int x,int y)
    {
        this.x = x;
        this.y = y;
    }

    public boolean isConnected(Position  other)
    {
        //if (this.x == other.x && this.y == other.y) return true; //same piece
        if (this.x+1 == other.x && this.y == other.y) return true;              //down
        else if (this.x-1 == other.x && this.y == other.y) return true;        //up
        else if (this.x == other.x && this.y+1 == other.y) return true;       //right
        else if (this.x == other.x && this.y-1 == other.y) return true;      //left

        else if (this.x+1 == other.x && this.y+1 == other.y)  return true;    //lower right
        else if (this.x-1 == other.x && this.y+1 == other.y)  return true;   //upper right
        else if (this.x-1 == other.x && this.y-1 == other.y)  return true;  //upper left
        else if (this.x+1 == other.x && this.y-1 == other.y)  return true; //lower left

        else return false; // not connected;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ") " ;
    }
}
