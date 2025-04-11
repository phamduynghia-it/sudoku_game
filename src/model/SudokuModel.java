package model;


public class SudokuModel {
    private static final int SIZE = 9;
    private static final int BOX_SIZE = 3;
    private int[][] board;
    private int[][] solution;
    private boolean[][] isFixedCell;
    private int numberOfHints = 5;

    public SudokuModel() {
        board = new int[SIZE][SIZE];
        solution = new int[SIZE][SIZE];
        isFixedCell = new boolean[SIZE][SIZE];
    }

    public int getNumberOfHints() {
        return numberOfHints;
    }
    public void setNumberOfHints(int numberOfHints) {
        this.numberOfHints = numberOfHints;
    }
    public int[][] getBoard() {
        return board;
    }
    public void setIsFixedCell(int r, int c, boolean b)
    {
        isFixedCell[r][c]= b;
    }
    public int[][] getSolution() {
        return solution;
    }
    public void setBoard(int i, int j, int value) {
        board[i][j] = value;
    }
    public void setBoard(int [][] board)
    {
        this.board = board;
    }

    public void setSolution(int[][] solution) {
        this.solution = solution;
    }

    public boolean[][] getFixedCells() {
        return isFixedCell;
    }
    public int getSize()
    {
        return SIZE;
    }
    public void setFixedCells(boolean [][] fixedCells) {
        this.isFixedCell = fixedCells;
    }



    public boolean isValidMove(int row, int column, int value) {

        for (int i = 0; i < SIZE; i++) {
            if ((board[row][i] == value && i != column) ||
                    (board[i][column] == value && i != row)) {
                return false;
            }
        }


        int boxRow = row - row % BOX_SIZE;
        int boxCol = column - column % BOX_SIZE;

        for (int i = boxRow; i < boxRow + BOX_SIZE; i++) {
            for (int j = boxCol; j < boxCol + BOX_SIZE; j++) {
                if (board[i][j] == value && (i != row || j != column)) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean isGameComplete() {
        for (int i = 0 ; i < SIZE; i++)
        {
            for (int j=0 ; j < SIZE; j++){
                if (board[i][j] == 0) return false;
            }
        }
        return true;
    }


}
