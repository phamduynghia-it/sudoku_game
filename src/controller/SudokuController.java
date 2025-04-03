package controller;
import model.SudokuModel;
import view.ISudokuView;
import java.awt.*;
import java.io.IOException;
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
        this.board = new int[9][9];
        this.solution = new int[9][9];
        this.fixedCells = new boolean[9][9];
    }

    public void changeBGWhileGameStarted() {
        view.changeBGCheckButton(Color.green);
        view.changeBGStartButton(Color.gray);
        view.changeBGSuggestButton(Color.yellow);
        view.changeBGAnswerButton(Color.gray);
        view.changeBGSaveButton(Color.red);
    }

    public void updateViewFromModel() {
        board = this.model.getBoard();
        solution = this.model.getSolution();
        fixedCells = this.model.getFixedCells();
        boolean isDarkMode = view.getDarkMode();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.view.updateCell(i, j, board[i][j], fixedCells[i][j], isDarkMode);
            }
        }
    }

    public void updateViewFromModel(boolean[][] fixed) {
        int[][] board = this.model.getBoard();
        fixedCells = fixed;
        solution= model.getSolution();
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
            model.loadRandomBoard();
            model.setNumberOfHints(5);
            updateViewFromModel();
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
                model.createParentDirectories("src/saveGame/sudoku_save.txt");
                model.createParentDirectories("src/saveGame/solution_save.txt");
                model.createParentDirectories("src/saveGame/fixedMatrix.txt");
                model.writeMatrixToFile("src/saveGame/sudoku_save.txt", currentBoard);
                model.writeMatrixToFile("src/saveGame/solution_save.txt", solution);
                model.writeBooleanMatrixToFile("src/saveGame/fixedMatrix.txt", fixedCells);
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
                boolean[][] fixedBoard = model.loadContinueBoard();
                updateViewFromModel(fixedBoard);
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
            boolean isDarkMode = view.getDarkMode();
            board = getBoardFromView();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    this.view.updateCell(i, j, board[i][j], fixedCells[i][j], isDarkMode);
                }
            }
        }
    }
}
