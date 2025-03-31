package model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class SudokuModel {
    static final int Size = 9;
    private int [][] Board;
    private int [][] Solution;
    private boolean[][] isFixedCell;


    public SudokuModel() {
        Board = new int[Size][Size];
        Solution = new int[Size][Size];
        isFixedCell = new boolean[9][9];
        loadRandomBoard();
    }

    public int[][] getBoard() {
        return Board;
    }

    public int[][] getSolution() {
        return Solution;
    }

    public void setBoard(int i, int j, int value) {
        Board[i][j] = value;
    }
    public static int[][] readSinglePuzzleFromFile(String filePath) {
        int[][] board = new int[Size][Size];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < Size) {
                String[] values = line.trim().split("\\s+");
                if (values.length != Size) {
                    System.out.println("Dòng có số lượng phần tử không hợp lệ: " + line);
                    continue;
                }
                for (int col = 0; col < Size; col++) {
                    board[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }

            if (row < Size) {
                System.out.println("Tệp không chứa đủ dữ liệu cho một ma trận Sudoku.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return board;
    }
    public static boolean[][] readBooleanMatrixFromFile(String filePath) {
        boolean[][] boolBoard = new boolean[Size][Size];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < Size) {
                String[] values = line.trim().split("\\s+");
                if (values.length != Size) {
                    System.out.println("Dòng có số lượng phần tử không hợp lệ: " + line);
                    continue;
                }
                for (int col = 0; col < Size; col++) {
                    boolBoard[row][col] = values[col].equals("1");
                }
                row++;
            }

            if (row < Size) {
                System.out.println("Tệp không chứa đủ dữ liệu cho một ma trận boolean.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boolBoard;
    }


    public void loadRandomBoard() {
        Random rand = new Random();
        int game = rand.nextInt(5) + 1;
        String boardPath = String.format("src/Level/sudokumatrix%d.txt", game);
        String solutionPath = String.format("src/Level/sudokumatrixSolution%d.txt", game);

        Board = readSinglePuzzleFromFile(boardPath);
        Solution = readSinglePuzzleFromFile(solutionPath);
    }

    public void saveGameToFile(String filePath, String fixedMatrixPath) {
        try (FileWriter writer = new FileWriter(filePath, false)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    writer.write(Board[i][j] + " ");
                }
                writer.write("\n");
            }
            System.out.println("Game đã được lưu thành công!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(fixedMatrixPath, false)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    writer.write((isFixedCell[i][j] ? "1" : "0") + " ");
                }
                writer.write("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean ValidMatrix(int row, int column, int value) {

        for (int i = 0; i < Size; i++) {
            if ((Board[row][i] == value && i != column) ||
                    (Board[i][column] == value && i != row)) {
                return false;
            }
        }

        int startRow = (row / 3) * 3;
        int startCol = (column / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (Board[i][j] == value && (i != row || j != column)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean[][] getFixedCells() {
        isFixedCell = new boolean[Size][Size];
        for (int row = 0; row < Size; row++) {
            for (int col = 0; col < Size; col++) {
                if (Board[row][col] != 0) {
                    isFixedCell[row][col] = true;
                }
            }
        }
        return isFixedCell;
    }
    public void setIsFixedCell(int r, int c, boolean b)
    {
        isFixedCell[r][c]= b;
    }
    public boolean endGame() {
        for (int i = 0 ; i < Size; i++)
        {
            for (int j=0 ; j < Size; j++){
                if (Board[i][j] == 0) return false;
            }
        }
        return true;
    }

}
