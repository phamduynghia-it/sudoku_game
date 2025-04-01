package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

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
    public boolean[][] getFixedCells() {
        isFixedCell = new boolean[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != 0) {
                    isFixedCell[row][col] = true;
                }
            }
        }
        return isFixedCell;
    }
    public void loadRandomBoard() throws IOException {
        Random rand = new Random();
        int game = rand.nextInt(5) + 1;
        Path boardPath = Paths.get("src", "Level", String.format("sudokumatrix%d.txt", game));
        Path solutionPath = Paths.get("src", "Level", String.format("sudokumatrixSolution%d.txt", game));

        if (!Files.exists(boardPath) || !Files.exists(solutionPath)) {
            throw new IOException("Không tìm thấy file dữ liệu Sudoku");
        }

        board = readPuzzleFromFile(boardPath.toString());
        solution = readPuzzleFromFile(solutionPath.toString());
        updateFixedCells();
    }
    public boolean[][] loadContinueBoard() throws IOException {
        board = readPuzzleFromFile("src/saveGame/sudoku_save.txt");
        isFixedCell=readBooleanMatrixFromFile("src/saveGame/fixedMatrix.txt");
        return isFixedCell;
    }
    private int[][] readPuzzleFromFile(String filePath) throws IOException {
        int[][] puzzle = new int[SIZE][SIZE];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < SIZE) {
                String[] values = line.trim().split("\\s+");
                if (values.length != SIZE) {
                    throw new IOException("Định dạng file không hợp lệ - số cột không đúng");
                }
                for (int col = 0; col < SIZE; col++) {
                    puzzle[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }

            if (row < SIZE) {
                throw new IOException("File không đủ dữ liệu - thiếu hàng");
            }
        } catch (NumberFormatException e) {
            throw new IOException("Dữ liệu không phải số nguyên", e);
        }
        return puzzle;
    }
    private boolean[][] readBooleanMatrixFromFile(String filePath) throws IOException {
        boolean[][] boolBoard = new boolean[SIZE][SIZE];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < SIZE) {
                String[] values = line.trim().split("\\s+");
                if (values.length != SIZE) {
                    throw new IOException("Định dạng file không hợp lệ - số cột không đúng");
                }
                for (int col = 0; col < SIZE; col++) {
                    if (!values[col].equals("0") && !values[col].equals("1")) {
                        throw new IOException("Dữ liệu boolean không hợp lệ");
                    }
                    boolBoard[row][col] = values[col].equals("1");
                }
                row++;
            }

            if (row < SIZE) {
                throw new IOException("File không đủ dữ liệu - thiếu hàng");
            }
        }
        return boolBoard;
    }
    private void updateFixedCells() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                isFixedCell[i][j] = (board[i][j] != 0);
            }
        }
    }
    public boolean isValidMove(int row, int column, int value) {
        // Kiểm tra hàng và cột
        for (int i = 0; i < SIZE; i++) {
            if ((board[row][i] == value && i != column) ||
                    (board[i][column] == value && i != row)) {
                return false;
            }
        }

        // Kiểm tra ô 3x3
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


    public void saveGameToFile(String filePath, String fixedMatrixPath) throws IOException {
        if (filePath == null || fixedMatrixPath == null) {
            throw new IllegalArgumentException("Đường dẫn file không được null");
        }
        createParentDirectories(filePath);
        createParentDirectories(fixedMatrixPath);
        writeMatrixToFile(filePath, board);
        writeBooleanMatrixToFile(fixedMatrixPath, isFixedCell);
    }
    private void createParentDirectories(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
    private void writeMatrixToFile(String filePath, int[][] matrix) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (int[] row : matrix) {
                for (int value : row) {
                    writer.write(value + " ");
                }
                writer.newLine();
            }
        }
    }
    private void writeBooleanMatrixToFile(String filePath, boolean[][] matrix) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (boolean[] row : matrix) {
                for (boolean value : row) {
                    writer.write(value ? "1 " : "0 ");
                }
                writer.newLine();
            }
        }
    }

}
