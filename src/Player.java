import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class Player {
    private int color;
    private int opponentColor;
    public int boardSize;
    private int turn;
    public int[][] board;
    ArrayList<MoveType> availableMovesType = new ArrayList<>();

    Player(int color)
    {
        this.color = color;
        opponentColor = (color%2) + 1;
        boardSize = 8;
        board = new int[boardSize][boardSize];
    }

    public void readFile(String path)
    {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            this.turn = Integer.parseInt(line);

            line = bufferedReader.readLine();
            int previous = Integer.parseInt(line);

            for(int i=0;i<this.boardSize;i++)
            {
                line = bufferedReader.readLine();
                String[] piece = line.split(" ");
                for(int j=0;j<this.boardSize;j++)
                {
                    this.board[i][j] = Integer.parseInt(piece[j]);
                }
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String path,int nextTurn)
    {
        try {
            FileWriter fileWriter = new FileWriter(path);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(nextTurn+"\n");
            bufferedWriter.write(this.color+"\n");

            for (int i=0;i<boardSize;i++)
            {
                for (int j=0;j<boardSize;j++)
                {
                    bufferedWriter.write(board[i][j]+"");
                    if(j != boardSize-1) bufferedWriter.write(" ");
                }
                if(i != boardSize-1 )bufferedWriter.write("\n");
            }

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int horizontalPieceCount(Position position)
    {
        int count = 0;
        //Horizontal (on a fix row) Piece Count
        for(int i=0;i<boardSize;i++)
        {
            if(board[position.x][i] == 1 || board[position.x][i] == 2) count++;
            //System.out.print("(" + position.x + ","  + i + ") ");
        }
        return count;
    }

    public int verticalPieceCount(Position position)
    {
        int count = 0;
        //Horizontal (on a fix column) Piece Count
        for(int i=0;i<boardSize;i++)
        {
            if(board[i][position.y] == 1 || board[i][position.y] == 2) count++;
            //System.out.print("(" + i + ","  + position.y + ") ");
        }
        return count;
    }

    public int firstDiagonalPieceCount(Position position)
    {
        int row,column;
        int count = 0;

        if(position.x >= position.y)
        {
            row = position.x - position.y;
            column = 0;
        }
        else
        {
            row = 0;
            column = position.y - position.x;
        }

        while (row < boardSize && column < boardSize )
        {
            //System.out.print("(" + row + ","  + column + ") ");
            if (board[row][column] == 1 || board[row][column] == 2) count++;
            row++;
            column++;
        }

        return count;
    }

    public int secondDiagonalPieceCount(Position position)
    {
        int row,column;
        int count = 0;

        if((position.x + position.y) < boardSize)
        {
           row = 0;
           column = position.x + position.y;
        }
        else
        {
            column = boardSize - 1;
            row = (position.x + position.y) - column;
        }

        while (row < boardSize && column >= 0)
        {
            //System.out.print("(" + row + ","  + column + ") ");
            if(board[row][column] == 1 || board[row][column] == 2) count++;
            row++;
            column--;
        }

        return count;
    }

    public ArrayList<Position> getAvailableMoves(Position position)
    {
//        System.out.println("row: " + horizontalPieceCount(position));
//        System.out.println("col: " + verticalPieceCount(position));
//        System.out.println("1st dig: " + firstDiagonalPieceCount(position));
//        System.out.println("2nd dig: " + secondDiagonalPieceCount(position));

        int pieceCount,movesTaken,row,column;
        ArrayList<Position> availableMoves = new ArrayList<>();
        availableMovesType.clear();

        if(board[position.x][position.y] != this.color)
        {
            System.out.println("Not our piece");
            return availableMoves;
        }

        //Right Move
        pieceCount = horizontalPieceCount(position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        column++;      // Moved Right
        while (column < boardSize)
        {
            if(board[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is the destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board[row][column] == 0)  movesTaken++;             //Check if empty
                                                                        //If position is same color --> continue
            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("Right Move" , pieceCount));
                break;
            }

            column++;      // Moved Right
        }

        //Left Move
        pieceCount = horizontalPieceCount(position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        column--;          // Moved Left
        while (column >= 0)
        {
            if(board[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board[row][column] == 0)  movesTaken++;             //Check if empty
                                                                        //If position is same color --> continue
            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("Left Move" , pieceCount));
                break;
            }

            column--;      // Moved Left
        }
        //Up Move
        pieceCount = verticalPieceCount(position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row--;          // Moved Up
        while (row >= 0)
        {
            if(board[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board[row][column] == 0)  movesTaken++;             //Check if empty
                                                                        //If position is same color --> continue
            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("Up Move" , pieceCount));
                break;
            }

            row--;      // Moved Up
        }

        //Down Move
        pieceCount = verticalPieceCount(position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row++;          // Moved Down
        while (row < boardSize)
        {
            if(board[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board[row][column] == 0)  movesTaken++;             //Check if empty
                                                                        //If position is same color --> continue
            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("Down Move" , pieceCount));
                break;
            }

            row++;      // Moved Down
        }
        //Lower Right Move
        pieceCount = firstDiagonalPieceCount(position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row++; column++;          // Moved Lower Right (1st diagonal down)
        while (row < boardSize && column < boardSize)
        {
            if(board[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board[row][column] == 0)  movesTaken++;             //Check if empty
                                                                        //If position is same color --> continue
            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("1st diagonal down Move" , pieceCount));
                break;
            }

            row++; column++;      // Moved Lower Right (1st diagonal down)
        }
        //Upper Left Move
        pieceCount = firstDiagonalPieceCount(position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row--; column--;          // Moved Upper Left (1st diagonal up)
        while (row >= 0 && column >= 0)
        {
            if(board[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board[row][column] == 0)  movesTaken++;             //Check if empty
            //If position is same color --> continue
            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("1st diagonal up Move" , pieceCount));
                break;
            }

            row--; column--;      // Moved Upper Left (1st diagonal up)
        }

        //Lower Left Move
        pieceCount = secondDiagonalPieceCount(position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row++; column--;          // Moved Lower Left (2nd diagonal down)
        while (row < boardSize && column >= 0)
        {
            if(board[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board[row][column] == 0)  movesTaken++;             //Check if empty
            //If position is same color --> continue
            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("2nd diagonal down Move" , pieceCount));
                break;
            }

            row++; column--;          // Moved Lower Left (2nd diagonal down)
        }

        //Upper Right Move
        pieceCount = secondDiagonalPieceCount(position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row--; column++;          // Moved Upper Right (2nd diagonal up)
        while (row >= 0 && column < boardSize)
        {
            if(board[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board[row][column] == 0)  movesTaken++;             //Check if empty
            //If position is same color --> continue
            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("2nd diagonal up Move" , pieceCount));
                break;
            }

            row--; column++;          // Moved Upper Right (2nd diagonal up)
        }



        return availableMoves;
    }

    public boolean isGameOver()
    {
        ArrayList<Position> playerPieces = new ArrayList<>();
        ArrayList<Position> connectedPieces = new ArrayList<>();
        int totalPiece = 0;

        for(int i=0;i<boardSize;i++)
        {
            for (int j=0;j<boardSize;j++)
            {
                if(board[i][j] == this.color)
                {
                    playerPieces.add(new Position(i,j));
                }
            }
        }

        totalPiece = playerPieces.size();

        if(playerPieces.size() == 1) return true;

        connectedPieces.add(playerPieces.get(0));
        playerPieces.remove(0);


        ArrayList<Position> temp = new ArrayList<>();
        while (true)
        {
            temp.clear();

            for (Position playerPiece : playerPieces)
            {
                for (Position connectedPiece : connectedPieces)
                {
                    if (playerPiece.isConnected(connectedPiece))
                    {
                        temp.add(playerPiece);
                        break;
                    }
                }

            }

            if(temp.size() == 0) return false;

            for(Position p: temp)
            {
                connectedPieces.add(p);
                playerPieces.remove(p);
            }

            if(connectedPieces.size() == totalPiece) return true;
        }
    }

    public void move()
    {
        Random random = new Random(System.currentTimeMillis());
        int i,j,x,y,p;
        while (true)
        {
            i = random.nextInt(8);
            j = random.nextInt(8);

            if(board[i][j] == color) {
                ArrayList<Position> possibleMoves = getAvailableMoves(new Position(i,j));
                if(possibleMoves.size() != 0)
                {
                    p = random.nextInt(possibleMoves.size());

                    x = possibleMoves.get(p).x;
                    y = possibleMoves.get(p).y;

                    Position from = new Position(i,j);
                    Position to  = new Position(x,y);

                    System.out.println(availableMovesType.get(p));
                    System.out.println("From: " + from + "to: " + to);

                    board[x][y] = color;
                    board[i][j] = 0;
                    break;
                }
            }
        }
    }
    public void printBoard()
    {
        for(int i=0;i<boardSize;i++){
            for (int j=0;j<boardSize;j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    public int getTurn()
    {
        return this.turn;
    }

    public int getColor()
    {
        return color;
    }

    public int getOpponentColor()
    {
        return opponentColor;
    }
}
