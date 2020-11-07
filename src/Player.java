import javax.sound.midi.Soundbank;
import java.io.*;
import java.util.*;

@SuppressWarnings("Duplicates")
public class Player {
    private int color;
    private int opponentColor;
    public int boardSize;
    private int turn;
    public Board board;
    public int[][] tempBoard;
    public int[][] positionalValueBoard;
    private ArrayList<MoveType> availableMovesType = new ArrayList<>();
    private ArrayList<Board> visitedBoard = new ArrayList<>();
    public  int hurChoice;

    Player(int color)
    {
        this.color = color;
        opponentColor = (color%2) + 1;
        boardSize = 6;
        tempBoard = new int[boardSize][boardSize];

        if (boardSize == 8)
        {
            positionalValueBoard = new int[][]{
                    {-80, -25, -20, -20, -20, -20, -25, -80},
                    {-25,  10,  10,  10,  10,  10,  10, -25},
                    {-20,  10,  25,  25,  25,  25,  10, -20},
                    {-20,  10,  25,  50,  50,  25,  10, -20},
                    {-20,  10,  25,  50,  50,  25,  10, -20},
                    {-20,  10,  25,  25,  25,  25,  10, -20},
                    {-25,  10,  10,  10,  10,  10,  10, -25},
                    {-80, -25, -20, -20, -20, -20, -25, -80}
            };
        }
        else if(boardSize == 6)
        {
            positionalValueBoard = new int [][]
                    {
                            {-80, -25, -20, -20, -25, -80},
                            {-25,  10,  10,  10,  10, -25},
                            {-20,  25,  50,  50,  25, -20},
                            {-20,  25,  25,  25,  25, -20},
                            {-25,  10,  10,  10,  10, -25},
                            {-80, -25, -20, -20, -25, -80}
                    };
        }
    }

    public boolean readFile(String path)
    {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while (!bufferedReader.ready()) ;

            String line = bufferedReader.readLine();
            if(line == null) return false;
            this.turn = Integer.parseInt(line);

            line = bufferedReader.readLine();
            int previous = Integer.parseInt(line);

            for(int i=0;i<this.boardSize;i++)
            {
                line = bufferedReader.readLine();
                String[] piece = line.split(" ");
                for(int j=0;j<this.boardSize;j++)
                {
                    this.tempBoard[i][j] = Integer.parseInt(piece[j]);
                }
            }
            this.board = new Board(this.tempBoard);

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void writeFile(String path,int nextTurn,Board board)
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
                    bufferedWriter.write(board.getBoard_()[i][j]+"");
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

    public int horizontalPieceCount(Board board,Position position)
    {
        int count = 0;
        //Horizontal (on a fix row) Piece Count
        for(int i=0;i<boardSize;i++)
        {
            if(board.getBoard_()[position.x][i] == 1 || board.getBoard_()[position.x][i] == 2) count++;
            //System.out.print("(" + position.x + ","  + i + ") ");
        }
        return count;
    }

    public int verticalPieceCount(Board board,Position position)
    {
        int count = 0;
        //Horizontal (on a fix column) Piece Count
        for(int i=0;i<boardSize;i++)
        {
            if(board.getBoard_()[i][position.y] == 1 || board.getBoard_()[i][position.y] == 2) count++;
            //System.out.print("(" + i + ","  + position.y + ") ");
        }
        return count;
    }

    public int firstDiagonalPieceCount(Board board,Position position)
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
            if (board.getBoard_()[row][column] == 1 || board.getBoard_()[row][column] == 2) count++;
            row++;
            column++;
        }

        return count;
    }

    public int secondDiagonalPieceCount(Board board,Position position)
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
            if(board.getBoard_()[row][column] == 1 || board.getBoard_()[row][column] == 2) count++;
            row++;
            column--;
        }

        return count;
    }

    public ArrayList<Position> getAvailableMoves(Board board,Position position,int playerColor,int opponentColor)
    {
//        System.out.println("row: " + horizontalPieceCount(position));
//        System.out.println("col: " + verticalPieceCount(position));
//        System.out.println("1st dig: " + firstDiagonalPieceCount(position));
//        System.out.println("2nd dig: " + secondDiagonalPieceCount(position));

        int pieceCount,movesTaken,row,column;
        ArrayList<Position> availableMoves = new ArrayList<>();
        availableMovesType.clear();

        if(board.getBoard_()[position.x][position.y] != playerColor)
        {
            System.out.println("Not our piece");
            return availableMoves;
        }

        //Right Move
        pieceCount = horizontalPieceCount(board,position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        column++;      // Moved Right
        while (column < boardSize)
        {
            if(board.getBoard_()[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is the destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board.getBoard_()[row][column] == playerColor)
            {
                if(movesTaken == pieceCount-1) break;            //Friendly piece in the destination --> can't move there
                else movesTaken++;                              //jump over friendly piece
            }
            else if(board.getBoard_()[row][column] == 0)  movesTaken++;             //Check if empty

            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("Right Move" , pieceCount));
                break;
            }

            column++;      // Moved Right
        }

        //Left Move
        pieceCount = horizontalPieceCount(board,position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        column--;          // Moved Left
        while (column >= 0)
        {
            if(board.getBoard_()[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board.getBoard_()[row][column] == playerColor)
            {
                if(movesTaken == pieceCount-1) break;            //Friendly piece in the destination --> can't move there
                else movesTaken++;                              //jump over friendly piece
            }
            else if(board.getBoard_()[row][column] == 0)  movesTaken++;             //Check if empty

            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("Left Move" , pieceCount));
                break;
            }

            column--;      // Moved Left
        }
        //Up Move
        pieceCount = verticalPieceCount(board,position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row--;          // Moved Up
        while (row >= 0)
        {
            if(board.getBoard_()[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board.getBoard_()[row][column] == playerColor)
            {
                if(movesTaken == pieceCount-1) break;            //Friendly piece in the destination --> can't move there
                else movesTaken++;                              //jump over friendly piece
            }
            else if(board.getBoard_()[row][column] == 0)  movesTaken++;             //Check if empty

            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("Up Move" , pieceCount));
                break;
            }

            row--;      // Moved Up
        }

        //Down Move
        pieceCount = verticalPieceCount(board,position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row++;          // Moved Down
        while (row < boardSize)
        {
            if(board.getBoard_()[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board.getBoard_()[row][column] == playerColor)
            {
                if(movesTaken == pieceCount-1) break;            //Friendly piece in the destination --> can't move there
                else movesTaken++;                              //jump over friendly piece
            }
            else if(board.getBoard_()[row][column] == 0)  movesTaken++;             //Check if empty

            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("Down Move" , pieceCount));
                break;
            }

            row++;      // Moved Down
        }
        //Lower Right Move
        pieceCount = firstDiagonalPieceCount(board,position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row++; column++;          // Moved Lower Right (1st diagonal down)
        while (row < boardSize && column < boardSize)
        {
            if(board.getBoard_()[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board.getBoard_()[row][column] == playerColor)
            {
                if(movesTaken == pieceCount-1) break;            //Friendly piece in the destination --> can't move there
                else movesTaken++;                              //jump over friendly piece
            }
            else if(board.getBoard_()[row][column] == 0)  movesTaken++;             //Check if empty

            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("1st diagonal down Move" , pieceCount));
                break;
            }

            row++; column++;      // Moved Lower Right (1st diagonal down)
        }
        //Upper Left Move
        pieceCount = firstDiagonalPieceCount(board,position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row--; column--;          // Moved Upper Left (1st diagonal up)
        while (row >= 0 && column >= 0)
        {
            if(board.getBoard_()[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board.getBoard_()[row][column] == playerColor)
            {
                if(movesTaken == pieceCount-1) break;            //Friendly piece in the destination --> can't move there
                else movesTaken++;                              //jump over friendly piece
            }
            else if(board.getBoard_()[row][column] == 0)  movesTaken++;             //Check if empty

            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("1st diagonal up Move" , pieceCount));
                break;
            }

            row--; column--;      // Moved Upper Left (1st diagonal up)
        }

        //Lower Left Move
        pieceCount = secondDiagonalPieceCount(board,position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row++; column--;          // Moved Lower Left (2nd diagonal down)
        while (row < boardSize && column >= 0)
        {
            if(board.getBoard_()[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board.getBoard_()[row][column] == playerColor)
            {
                if(movesTaken == pieceCount-1) break;            //Friendly piece in the destination --> can't move there
                else movesTaken++;                              //jump over friendly piece
            }
            else if(board.getBoard_()[row][column] == 0)  movesTaken++;             //Check if empty

            if(movesTaken == pieceCount)
            {
                availableMoves.add(new Position(row,column));
                availableMovesType.add(new MoveType("2nd diagonal down Move" , pieceCount));
                break;
            }

            row++; column--;          // Moved Lower Left (2nd diagonal down)
        }

        //Upper Right Move
        pieceCount = secondDiagonalPieceCount(board,position);
        row = position.x;
        column = position.y;
        movesTaken = 0;

        row--; column++;          // Moved Upper Right (2nd diagonal up)
        while (row >= 0 && column < boardSize)
        {
            if(board.getBoard_()[row][column] == opponentColor)
            {
                if(movesTaken == pieceCount-1) movesTaken++;            //Take opponent's piece as it is destination
                else break;                                             //Otherwise blocked; Piece can not move
            }
            else if(board.getBoard_()[row][column] == playerColor)
            {
                if(movesTaken == pieceCount-1) break;            //Friendly piece in the destination --> can't move there
                else movesTaken++;                              //jump over friendly piece
            }
            else if(board.getBoard_()[row][column] == 0)  movesTaken++;             //Check if empty

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

    public ArrayList<Position> getPiecesOfSameColor(Board board,int color)
    {
        ArrayList<Position> pieces = new ArrayList<>();

        for(int i=0;i<boardSize;i++)
        {
            for (int j=0;j<boardSize;j++)
            {
                if(board.getBoard_()[i][j] == color)
                {
                    pieces.add(new Position(i,j));
                }
            }
        }

        return pieces;
    }

    public boolean isGameOver(Board board,int color)
    {
        ArrayList<Position> connectedPieces = new ArrayList<>();

        ArrayList<Position> playerPieces = getPiecesOfSameColor(board,color);
        int totalPiece = playerPieces.size();

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

    public int positionalValueEVAL(Board board,int color)
    {
        int boardVal = 0;
        for(int i=0;i<boardSize;i++)
        {
            for (int j=0;j<boardSize;j++)
            {
                if(board.getBoard_()[i][j] == color)
                {
//                    System.out.println("v: " + positionalValueBoard[i][j]);
                    boardVal += this.positionalValueBoard[i][j];
                }
            }
        }
        return boardVal;
    }

    public int mobilityEVAL(Board board,int color)
    {
        int moveCount = 0;
        int captureMove = 0;

        int oppoColor = (color%2) + 1;

        for(int i=0;i<boardSize;i++)
        {
            for (int j=0;j<boardSize;j++)
            {
                if(board.getBoard_()[i][j] == color)
                {
//                    System.out.print("cur piece: " + new Position(i,j) + ": ");
                    ArrayList<Position> availableMoves = getAvailableMoves(board,new Position(i,j),color,oppoColor);

                    moveCount += availableMoves.size();
//                    for(Position p : availableMoves)
//                    {
//                        System.out.print(p);
//                        //For count capture moves
////                        if(board.getBoard_()[p.x][p.y] == this.getOpponentColor())
////                        {
////                            moveCount++;
////                            System.out.print("(C) ");
////                        }
//                    }
//                    System.out.println();
                }
            }
        }
        return moveCount;
    }

    public double areaEVAL(Board board,int color)
    {
        int top     = boardSize;    //top   --> min row; init as  8
        int bottom  = -1;          //bottom --> max row; init as -1
        int left    = boardSize;  //left    --> min col; init as  8
        int right   = -1;        //right    --> max col; init as -1

        for (int i=0;i<boardSize;i++)
        {
            for (int j=0;j<boardSize;j++)
            {
                if(board.getBoard_()[i][j] == color)
                {
                    top    = Math.min(top,i);
                    bottom = Math.max(bottom,i);
                    left   = Math.min(left,j);
                    right  = Math.max(right,j);
                }
            }
        }

        double area = (Math.abs(top-bottom)+1) * (Math.abs(left-right)+1);

        return -area;
    }

    public double connectedEVAL(Board board,int color)
    {
        ArrayList<Position> playerPieces = getPiecesOfSameColor(board,color);
        int totalPiece = playerPieces.size();
        int connections = 0;
        for(Position piece: playerPieces)
        {
            for (Position check:playerPieces)
            {
                if(piece.isConnected(check)){
                    connections++;
                    break;
                }
            }
        }
        return ((double)connections/totalPiece);
    }

    public double densityEVAL(Board board,int color)
    {
        ArrayList<Position> playerPieces = getPiecesOfSameColor(board,color);

        int sumX = 0;
        int sumY = 0;
        int totalPiece = playerPieces.size();

        for(Position piece: playerPieces)
        {
            sumX += piece.x;
            sumY += piece.y;
        }

        int centerX = sumX / totalPiece;
        int centerY = sumY / totalPiece;

        double distance = 0;
        for(Position piece: playerPieces)
        {
            double d = ((centerX-piece.x)*(centerX-piece.x)) + ((centerY-piece.y)*(centerY-piece.y));
            distance += d;
        }

        return -(distance/totalPiece);
    }

    public double quadEVAL(Board board,int color)
    {
        ArrayList<Position> playerPieces = getPiecesOfSameColor(board,color);

        int sumX = 0;
        int sumY = 0;
        int totalPiece = playerPieces.size();

        for(Position piece: playerPieces)
        {
            sumX += piece.x;
            sumY += piece.y;
        }

        int centerX = sumX / totalPiece;
        int centerY = sumY / totalPiece;

        int row = Math.max(centerX - 2,0);
        int col = Math.max(centerY - 2,0);

        int q3Count = 0; // number of Q3 quad
        int q4Count = 0; // number of Q4 quad
        while(row < boardSize-1 && Math.abs(centerX-row)<=2)
        {
            while(col < boardSize-1 && Math.abs(centerY-col)<=2)
            {
                int count = 0; //pieces inside quad

                if(board.getBoard_()[row][col] == color) count++;
                if(board.getBoard_()[row][col+1] == color) count++;
                if(board.getBoard_()[row+1][col] == color) count++;
                if(board.getBoard_()[row+1][col+1] == color) count++;

                if(count == 3) q3Count++;
                else if(count == 4) q4Count++;

                col++; //new quad starting position on previous row
            }
            row++;  //one step down
            col = Math.max((centerY-2),0); //reset column
        }

        return (q3Count + 1.2*q4Count);
    }



    public Board findBestMove(Board board,double timeout)
    {
        double bestValue = Integer.MIN_VALUE;
        Board bestBoard = new Board(boardSize);

        ArrayList<Position> playerPieces = getPiecesOfSameColor(board,this.color);
        Collections.shuffle(playerPieces); //Randomization

        long startTime = System.nanoTime();
        long elapsedTime = 0;
        int depth = 0;

        while (elapsedTime<=timeout) {
            depth++;
            for (Position piece : playerPieces) {
                ArrayList<Position> validMoves = getAvailableMoves(board, piece, this.color, this.opponentColor);
                Collections.shuffle(validMoves);  //Randomization
                for (Position move : validMoves) {
                    Board tempBoard = new Board(this.boardSize);
                    tempBoard.copy(board);

                    tempBoard.getBoard_()[move.x][move.y] = this.color;
                    tempBoard.getBoard_()[piece.x][piece.y] = 0;

                    double tempVal = minmax(tempBoard, false, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);

                    if (tempVal > bestValue) {
                        bestValue = tempVal;
                        bestBoard.copy(tempBoard);
                    }

                    long endTime = System.nanoTime();
                    elapsedTime = (endTime - startTime)/1000000;

                    if(elapsedTime > timeout)
                    {
                        System.out.println("Elapsed Time: " + elapsedTime + " Depth: " + (depth-1));
                        System.out.println("best Value: " + bestValue);
                        System.out.println("best board: ");
                        printBoard(bestBoard);
                        return  bestBoard;
                    }
                }
            }
        }

        System.out.println("Elapsed Time: " + elapsedTime + " Depth: " + depth);
        System.out.println("best Value: " + bestValue);
        System.out.println("best board: ");
        printBoard(bestBoard);
        return bestBoard;
    }

    public double minmax(Board board,boolean isMaximizing,int depth,double alpha,double beta)
    {
        //System.out.println("depth: " + depth);


        if(depth == 0)
        {
            if(hurChoice == 1) return (0.2*areaEVAL(board,this.color)+
                    0.5*connectedEVAL(board,color)+
                    0.3*positionalValueEVAL(board,this.color)+
                    0.7*densityEVAL(board,this.color)+
                    0.25*quadEVAL(board,this.color)
                    +depth);

            else if(hurChoice == 2) return (0.2*areaEVAL(board,this.color)+
                    0.5*connectedEVAL(board,color)+
                    0.3*positionalValueEVAL(board,this.color)+
                    0.7*densityEVAL(board,this.color)+
                    0.5*quadEVAL(board,this.color)
                    +depth);
            else if(hurChoice == 3) return (quadEVAL(board,this.color)+depth);
        }
//        if(depth == 0) return positionalValueEVAL(board,this.color)+depth;

        if(isGameOver(board,this.getColor()))
        {
            return 5000+depth;
        }
        else if(isGameOver(board,this.getOpponentColor()))
        {
            return -5000-depth;
        }

        if(isMaximizing)
        {
            double bestVal = Integer.MIN_VALUE;

            ArrayList<Position> playerPieces = getPiecesOfSameColor(board,this.getColor());
            for (Position piece: playerPieces)
            {
                ArrayList<Position> validMoves = getAvailableMoves(board,piece,this.color,this.opponentColor);
                for(Position move: validMoves)
                {
                    Board tempBoard = new Board(this.boardSize);
                    tempBoard.copy(board);

                    tempBoard.getBoard_()[move.x][move.y] = this.getColor();
                    tempBoard.getBoard_()[piece.x][piece.y] = 0;

//                    if(!visitedBoard.contains(tempBoard))
//                        visitedBoard.add(tempBoard);
//                    else continue;


//                    System.out.println("Original board");
//                    printBoard(board);
//                    System.out.println("After Move" + "Depth " + depth + " From: " + piece + "To: " + move);
//                    printBoard(tempBoard);

                    double val = minmax(tempBoard,false,depth-1,alpha,beta);

                    bestVal = Math.max(bestVal,val);
                    alpha = Math.max(bestVal,alpha);

                    if(beta<=alpha) return bestVal;
                }
            }
            return bestVal;
        }

        else
        {
            double bestVal = Integer.MAX_VALUE;

            ArrayList<Position> playerPieces = getPiecesOfSameColor(board,this.getOpponentColor());
            for (Position piece: playerPieces)
            {
                ArrayList<Position> validMoves = getAvailableMoves(board,piece,this.opponentColor,this.color);
                for(Position move: validMoves)
                {
                    Board tempBoard = new Board(this.boardSize);
                    tempBoard.copy(board);

                    tempBoard.getBoard_()[move.x][move.y] = this.getOpponentColor();
                    tempBoard.getBoard_()[piece.x][piece.y] = 0;

//                    System.out.println("Original board");
//                    printBoard(board);
//                    System.out.println("After Move" + "Depth " + depth + " From: " + piece + "To: " + move);
//                    printBoard(tempBoard);

                    double val = minmax(tempBoard,true,depth-1,alpha,beta);

                    bestVal = Math.min(bestVal,val);
                    beta = Math.min(bestVal,beta);

                    if(beta<=alpha) return bestVal;
                }
            }
            return bestVal;
        }
    }

    public Board move(Board board)
    {
        Random random = new Random(System.currentTimeMillis());
        int i,j,x,y,p;
        while (true)
        {
            i = random.nextInt(boardSize);
            j = random.nextInt(boardSize);

            if(board.getBoard_()[i][j] == color) {
                ArrayList<Position> possibleMoves = getAvailableMoves(this.board,new Position(i,j),this.color,this.opponentColor);
                if(possibleMoves.size() != 0)
                {
                    p = random.nextInt(possibleMoves.size());

                    x = possibleMoves.get(p).x;
                    y = possibleMoves.get(p).y;

                    Position from = new Position(i,j);
                    Position to  = new Position(x,y);

                    System.out.println(availableMovesType.get(p));
                    System.out.println("From: " + from + "to: " + to);

                    board.getBoard_()[x][y] = color;
                    board.getBoard_()[i][j] = 0;
                    break;
                }
            }
        }
        return board;
    }
    public void printBoard(Board board)
    {
        System.out.println("-----------Start--------------");
        for(int i=0;i<boardSize;i++){
            for (int j=0;j<boardSize;j++){
                System.out.print(board.getBoard_()[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-----------Finish-------------");
    }

    public int getTurn()
    {
        return this.turn;
    }

    public int getColor()
    {
        return this.color;
    }

    public int getOpponentColor()
    {
        return this.opponentColor;
    }
}
