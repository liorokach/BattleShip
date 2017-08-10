package com.battleship.Logic;

public class Board {
    static final int EMPTY_CELL = -1;
    static final int MINE = -2;
    static final int HIT = -3;
    static final int MISS = -4;
    static final int ROW = 0;
    private int[][] matrix;
    private int matrixSize;

    Board(int size) { //empty board - enemy board
        this.matrixSize = size;
        matrix = new int[matrixSize][];
        for (int i = 0 ;i < matrixSize ;i++)
        {
            matrix[i] = new int[matrixSize];
        }
        for (int i = 0 ;i < matrixSize ;i++)
        {
            for(int j = 0 ;j < matrixSize ;j++)
            {
                matrix[i][j] = EMPTY_CELL;
            }
        }
    }

    public Board(int size ,Ship[] ships) throws GameException { //my board
        this(size);
        Point p[];
        for (int i = 0; i < ships.length; i++) {
            p = ships[i].getLoc();
            for (int j = 0; j < ships[i].getCount(); j++) {
                try {
                    matrix[p[j].getX()][p[j].getY()] = i;
                } catch (Exception exc) {// out of range exception
                    GameException ex = new GameException(exc.getMessage());
                    ex.setMsg(String.format("This Board isn't valid, you tried to put a ship in a place(%d,%d) that doesn't exist" +
                            " in the board with squares between (1,1) to(%d,%d)", p[i].getX() + 1, p[j].getY() + 1, matrixSize, matrixSize));
                    throw ex;
                }
            }
        }
    }

    public int getSize() {
        return matrixSize;
    }

    public boolean isHit(int x, int y) throws Exception {
        try {
            if (matrix[ x ][ y ] >= 0 || matrix[ x ][ y ] == MINE) {
                return true;
            }
            return false;
        } catch (Exception exc) {// out of range exception
            GameException ex = new GameException(exc.getMessage());
            ex.setMsg(String.format("you tried to attack in a place (%d,%d) that doesn't exist in the board," +
                    "valid input is between (1,1) to (%d,%d)", x + 1, y + 1, matrixSize,matrixSize),x+1,y+1);
            throw ex;
        }
    }

    public int getSquare(int x,int y) {
        return matrix[x][y];
    }

    public boolean isValidBoard(int shipsSquares) throws GameException {
        int countShipsSquares = 0;
        try {
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    if (matrix[ i ][ j ] >= 0) { //is ship
                        countShipsSquares++;
                        if (!isValidPlace(i, j, matrix[ i ][ j ])) // check if this ship doesn't collide with other ships
                            return false;
                    }
                }
            }
        } catch (GameException ex) {// out of range exception
            ex.setMsg(String.format("This Board isn't valid, you tried to put a ship in a place(%d,%d)," +
                    " that doesn't exist in the board",ex.getX(),ex.getY()));
            throw ex;
        }
        return countShipsSquares == shipsSquares;
    }

    public boolean isValidPlace(int x , int y, int validType) throws GameException {
        try {
            if (matrix[x][y] != EMPTY_CELL && matrix[x][y] != validType)
                return false;
        } catch (Exception exc) {// out of range exception
            GameException ex = new GameException(exc.getMessage());
            ex.setMsg(String.format("you tried to attack in a place (%d,%d) that doesn't exist in the board," +
                    "valid input is between (1,1) to (%d,%d)", x + 1, y + 1, matrixSize,matrixSize), x + 1, y + 1);
            throw ex;
        }
        for (int i = x - 1; i < x + 1; i++) {
            for (int j = y - 1; j < y + 1; j++) {
                if (!(i < 0 || i >= matrixSize || j < 0 || j >= matrixSize)) {
                    if (matrix[i][j] != validType && matrix[i][j] != EMPTY_CELL) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void addMine(int x , int y) {
        matrix[x][y] = MINE;
    }

    public void updateTheBoard(int x, int y , int n) {
        matrix[x][y] = n;
    }

    public boolean alreadyChecked(int x, int y) {
        if(matrix[x][y] == MISS || matrix[x][y] == HIT)
            return true;
        return false;
    }
}