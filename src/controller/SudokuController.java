package controller;
import model.SudokuModel;
import view.ISudokuView;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class SudokuController {
    private SudokuModel model;
    private ISudokuView view;
    private boolean[][] fixedCells;
    private int[][] solution;
    private int[][] board;

    public SudokuController(SudokuModel model, ISudokuView view) {
        this.model = model;
        this.view = view;
    }
    public boolean[][] updateFixedCells(){
        int SIZE = model.getSize();
        boolean[][] tempBoard = new boolean[9][9];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tempBoard[i][j] = (board[i][j] != 0);
            }
        }
        return tempBoard;
    }
    public void changeBGWhileGameStarted() {
        view.changeBGCheckButton(Color.green);
        view.changeBGStartButton(Color.gray);
        view.changeBGSuggestButton(Color.yellow);
        view.changeBGAnswerButton(Color.gray);
        view.changeBGSaveButton(Color.red);
    }
    public void setModelBoard(boolean[][] fixedCells, int[][] board, int[][] solution){
        this.model.setFixedCells(fixedCells);
        this.model.setBoard(board);
        this.model.setSolution(solution);
    }
    private int [][] readPuzzleFromFile(String filePath) throws IOException {
        int SIZE = model.getSize();
        int[][] tempBoard = new int[SIZE][SIZE];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < SIZE) {
                String[] values = line.trim().split("\\s+");
                if (values.length != SIZE) {
                    throw new IOException("Định dạng file không hợp lệ - số cột không đúng");
                }
                for (int col = 0; col < SIZE; col++) {
                    tempBoard[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }

            if (row < SIZE) {
                throw new IOException("File không đủ dữ liệu - thiếu hàng");
            }
        } catch (NumberFormatException e) {
            throw new IOException("Dữ liệu không phải số nguyên", e);
        }
        return tempBoard;
    }
    private boolean[][] readBooleanMatrixFromFile(String filePath) throws IOException {
        int SIZE = model.getSize();
        fixedCells = new boolean[SIZE][SIZE];
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
                    fixedCells[row][col] = values[col].equals("1");
                }
                row++;
            }

            if (row < SIZE) {
                throw new IOException("File không đủ dữ liệu - thiếu hàng");
            }
        }
        return fixedCells;
    }
    public void createParentDirectories(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
    public void writeMatrixToFile(String filePath, int[][] matrix) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (int[] row : matrix) {
                for (int value : row) {
                    writer.write(value + " ");
                }
                writer.newLine();
            }
        }
    }
    public void writeBooleanMatrixToFile(String filePath, boolean[][] matrix) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (boolean[] row : matrix) {
                for (boolean value : row) {
                    writer.write(value ? "1 " : "0 ");
                }
                writer.newLine();
            }
        }
    }
    // load game khi an choi moi
    public void loadRandomBoard() throws IOException {
        int SIZE = model.getSize();
        Random rand = new Random();
        int game = rand.nextInt(5) + 1;
        Path boardPath = Paths.get("src", "Level", String.format("sudokumatrix%d.txt", game));
        Path solutionPath = Paths.get("src", "Level", String.format("sudokumatrixSolution%d.txt", game));

        if (!Files.exists(boardPath) || !Files.exists(solutionPath)) {
            throw new IOException("Không tìm thấy file dữ liệu Sudoku");
        }

        board = readPuzzleFromFile(boardPath.toString());
        solution = readPuzzleFromFile(solutionPath.toString());
        fixedCells= updateFixedCells();
        this.setModelBoard(fixedCells, board, solution);
    }
    // load choi tiep tuc game da luu
    public void loadContinueBoard() throws IOException {
        board = readPuzzleFromFile("src/saveGame/sudoku_save.txt");
        fixedCells=readBooleanMatrixFromFile("src/saveGame/fixedMatrix.txt");
        solution=readPuzzleFromFile("src/saveGame/solution_save.txt");
        this.setModelBoard(fixedCells, board, solution);
    }
    public void loadSudokuBoard() {
        boolean isDarkMode = view.getDarkMode();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.view.updateCell(i, j, board[i][j], fixedCells[i][j], isDarkMode);
            }
        }
    }



    public int[][] getBoardFromView() throws NumberFormatException {
        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = view.getTextFromCell(i, j).trim();

                if (text.isEmpty()) {
                    board[i][j] = 0;
                } else {
                    try {
                        int value = Integer.parseInt(text);
                        if (value < 1 || value > 9) {
                            throw new NumberFormatException("Giá trị tại [" + i + "," + j + "] phải từ 1-9");
                        }
                        board[i][j] = value;
                    } catch (NumberFormatException e) {
                        view.highlightCell(i, j, Color.RED);
                        throw new NumberFormatException("Ô [" + i + "," + j + "] chứa giá trị không hợp lệ: " + text);
                    }
                }
            }
        }

        return board;
    }

    public void handleNewGame() {
        try {
            this.loadRandomBoard();
            model.setNumberOfHints(5);
            this.loadSudokuBoard();
            view.setGameControlsEnabled(true);
            view.showMessage("Game mới đã bắt đầu!", Color.BLUE);
            view.showNumberOfHints("Bạn còn " + model.getNumberOfHints() + " lượt gợi ý!", Color.black);
            changeBGWhileGameStarted();
        } catch (IOException ex) {
            view.showMessage("Lỗi khi tải game mới: " + ex.getMessage(), Color.RED);
        }
    }

    public void handleCheckGame() {
        if (view.getGameStarted()) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (!fixedCells[i][j]) {
                        String input = this.view.getTextFromCell(i, j).trim();

                        if (!input.isEmpty()) {
                            try {
                                int temp = Integer.parseInt(input);
                                if (temp >= 10 || temp <= 0) {
                                    view.showMessage("Chỉ được phép nhập số từ 1-9", Color.RED);
                                    view.flashCellBorder(i, j, Color.RED, 200);

                                } else if (!this.model.isValidMove(i, j, temp)) {
                                    view.flashCellBorder(i, j, Color.RED, 200);

                                } else {
                                    this.model.setBoard(i, j, temp);
                                }
                            } catch (NumberFormatException e) {
                                view.flashCellBorder(i, j, Color.RED, 200);

                            }
                        } else {
                            this.model.setBoard(i, j, 0);
                        }
                    }
                }
            }

            if (model.isGameComplete()) {
                view.showMessage("Chúc mừng! Bạn đã hoàn thành!", Color.GREEN);
            }
        }
    }

    public void handleHint() {
        if (view.getGameStarted()) {
            boolean isDarkMode = view.getDarkMode();
            int numberOfHints = this.model.getNumberOfHints();
            if (numberOfHints != 0) {
                Random rand = new Random();
                int r, c;
                do {
                    r = rand.nextInt(9);
                    c = rand.nextInt(9);
                } while (fixedCells[r][c]);
                this.model.setBoard(r, c, solution[r][c]);
                this.fixedCells[r][c] = true;
                this.model.setIsFixedCell(r, c, true);
                this.view.updateCell(r, c, solution[r][c], true, isDarkMode);
                this.view.highlightCell(r, c, Color.yellow);
                this.model.setNumberOfHints(--numberOfHints);
                view.showNumberOfHints("Bạn còn " + numberOfHints + " lượt gợi ý!", Color.black);
                if (numberOfHints == 0) {
                    view.changeBGSuggestButton(Color.gray);
                    view.changeBGAnswerButton(Color.yellow);
                }
            }
        }
    }

    public void handleSaveGame() throws IOException {
        if (view.getGameStarted()) {
            boolean isDarkMode = view.getDarkMode();
            try {
                int[][] currentBoard = getBoardFromView();
                view.showMessage("Đã lưu game thành công", Color.green);
                createParentDirectories("src/saveGame/sudoku_save.txt");
                createParentDirectories("src/saveGame/solution_save.txt");
                createParentDirectories("src/saveGame/fixedMatrix.txt");
                writeMatrixToFile("src/saveGame/sudoku_save.txt", currentBoard);
                writeMatrixToFile("src/saveGame/solution_save.txt", solution);
                writeBooleanMatrixToFile("src/saveGame/fixedMatrix.txt", fixedCells);
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        view.updateCell(i, j, 0, false, isDarkMode);
                        view.setCellEditable(i, j, false);
                    }
                }
                view.setGameControlsEnabled(false);
            } catch (NumberFormatException e) {
                view.showMessage(e.getMessage(), Color.RED);
            }
        }
    }

    public void handleContinueGame() {
        if (!view.getGameStarted()) {
            try {
                loadContinueBoard();
               this.loadSudokuBoard();
                view.setGameControlsEnabled(true);
                view.showMessage("Đã tải game tiếp thành công", Color.GREEN);
                changeBGWhileGameStarted();
            } catch (IOException e) {
                view.showMessage("Lỗi khi tải game: " + e.getMessage(), Color.RED);
                handleNewGame();
            }
        }
    }
    public void handleShowAnswer() {
        if (model.getNumberOfHints() == 0 && view.getGameStarted()) {
            boolean isDarkMode = view.getDarkMode();
            int[][] solution = model.getSolution();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    this.view.updateCell(i, j, solution[i][j], solution[i][j] != 0, isDarkMode);
                    view.highlightCell(i, j, Color.GREEN);
                }
            }
            view.changeBGCheckButton(Color.gray);
            view.changeBGStartButton(Color.red);
            view.changeBGAnswerButton(Color.gray);
            view.changeBGSaveButton(Color.gray);
            view.setGameControlsEnabled(false);
        }

    }

    public void handleDarkMode() {
        if (view.getGameStarted()) {
            if (!view.getDarkMode()) {
                view.setDarkMode(true);
                view.changeBGDarkModeButton(Color.white);
                view.setTextDarkModeButton("<html>chế độ <br>sáng<html>", Color.black);
            } else {
                view.setDarkMode(false);
                view.changeBGDarkModeButton(Color.black);
                view.setTextDarkModeButton("<html>chế độ <br>tối<html>", Color.white);
            }
            board = getBoardFromView();
            loadSudokuBoard();
        }
    }
}
