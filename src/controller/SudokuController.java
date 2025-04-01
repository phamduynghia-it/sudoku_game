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
    private int [][] solution;

    public SudokuController(SudokuModel model, ISudokuView view) {
        this.model = model;
        this.view = view;
    }

    public void updateViewFromModel() {
        int[][] board = this.model.getBoard();
        fixedCells = this.model.getFixedCells();
        solution = this.model.getSolution();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.view.updateCell(i, j, board[i][j], board[i][j] != 0);
            }
        }
    }
    public void updateViewFromModel(boolean [][] fixed) {
        int[][] board = this.model.getBoard();
        fixedCells = fixed;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.view.updateCell(i, j, board[i][j], fixedCells[i][j]);

                if (board[i][j] != 0 && !fixedCells[i][j]) {
                    this.view.highlightCell(i, j, Color.GREEN);
                }
            }
        }
    }

    public void handleNewGame() {
        try {
            model.loadRandomBoard();
            model.setNumberOfHints(5);
            updateViewFromModel();
            view.setGameControlsEnabled(true);
            view.showMessage("Game mới đã bắt đầu!", Color.BLUE);
            view.showNumberOfHints("Bạn còn " + model.getNumberOfHints() + " lượt gợi ý!", Color.black);
            view.changeBGCheckButton(Color.green);
            view.changeBGStartButton(Color.gray);
            view.changeBGSuggestButton(Color.yellow);
        } catch (IOException ex) {
            view.showMessage("Lỗi khi tải game mới: " + ex.getMessage(), Color.RED);
        }
    }

    public void handleCheckGame() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.view.getTextFromCell(i, j);
                if (!fixedCells[i][j]) {
                    String input = this.view.getTextFromCell(i, j).trim();
                    if (!input.isEmpty()) {
                        try {
                            int temp = Integer.parseInt(input);
                            if (temp > 10 || temp < 0){
                                view.showMessage("Chỉ được phép nhập số từ 1-9", Color.RED);
                            }
                            if (this.model.isValidMove(i, j, temp) && temp < 10 && temp > 0) {
                                this.view.highlightCell(i, j, Color.green);
                                this.model.setBoard(i, j, temp);
                            } else {
                                this.view.highlightCell(i, j, Color.red);
                            }
                        } catch (NumberFormatException e) {
                            this.view.highlightCell(i, j, Color.red);
                        }
                    } else {
                        this.view.highlightCell(i, j, Color.white);
                        this.model.setBoard(i, j, 0);
                    }
                }
            }
        }
        if (model.isGameComplete()) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    this.view.setCellEditable(i, j, false);
                    this.view.highlightCell(i, j, Color.green);
                }
            }
            view.showMessage("Chúc mừng! Bạn đã hoàn thành!", Color.GREEN);
            }
    }

    public void handleHint() {
        int numberOfHints = this.model.getNumberOfHints();
        if(numberOfHints == 0){
            return;
        }else {
            Random rand = new Random();
            int r, c;
            do {
                r = rand.nextInt(9);
                c = rand.nextInt(9);
            } while (fixedCells[r][c]);
            this.model.setBoard(r, c, solution[r][c]);
            this.fixedCells[r][c] = true;
            this.model.setIsFixedCell(r, c ,true);
            this.view.updateCell(r, c, solution[r][c], true);
           this.view.highlightCell(r, c, Color.yellow);
            this.model.setNumberOfHints(--numberOfHints);
            view.showNumberOfHints("Bạn còn " + numberOfHints + " lượt gợi ý!", Color.black);
            if (numberOfHints== 0)
                view.changeBGSuggestButton(Color.gray);
        }
    }
    public void handleSaveGame() throws IOException {
        if (view.getGameStarted()){
           view.showMessage("Đã lưu game thành công", Color.green);
            view.setGameControlsEnabled(false);
            model.saveGameToFile("src/saveGame/sudoku_save.txt","src/saveGame/fixedMatrix.txt" );
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    view.updateCell(i, j, 0, false);
                    view.setCellEditable(i, j, false);
                }
            }
        }
    }
    public void handleContinueGame() throws IOException {
        if(!view.getGameStarted()){
            view.setGameControlsEnabled(true);
            boolean[][] fixedBoard = model.loadContinueBoard();
            updateViewFromModel(fixedBoard);
        }
    }
}