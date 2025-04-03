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
    private  int [][] board;

    public SudokuController(SudokuModel model, ISudokuView view) {
        this.model = model;
        this.view = view;
    }
    public void changeBGWhileGameStarted(){
        view.changeBGCheckButton(Color.green);
        view.changeBGStartButton(Color.gray);
        view.changeBGSuggestButton(Color.yellow);
        view.changeBGAnswerButton(Color.gray);
        view.changeBGSaveButton(Color.red);
    }
    public void loadSudokuBoard(){
        boolean isDarkMode = view.getDarkMode();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.view.updateCell(i, j, board[i][j], fixedCells[i][j], isDarkMode);
            }
        }
    }
    public void updateViewFromModel() {
        board = this.model.getBoard();
        solution = this.model.getSolution();
        fixedCells = this.model.getFixedCells();
       loadSudokuBoard();
    }
    public void updateViewFromModel(boolean [][] fixed) {
        int[][] board = this.model.getBoard();
        fixedCells = fixed;
        boolean isDarkMode = view.getDarkMode();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.view.updateCell(i, j, board[i][j], fixedCells[i][j], isDarkMode);
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

                                }
                                else if (!this.model.isValidMove(i, j, temp)) {
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
       if (view.getGameStarted()){
           boolean isDarkMode = view.getDarkMode();
           int numberOfHints = this.model.getNumberOfHints();
           if(numberOfHints != 0){
               Random rand = new Random();
               int r, c;
               do {
                   r = rand.nextInt(9);
                   c = rand.nextInt(9);
               } while (fixedCells[r][c]);
               this.model.setBoard(r, c, solution[r][c]);
               this.fixedCells[r][c] = true;
               this.model.setIsFixedCell(r, c ,true);
               this.view.updateCell(r, c, solution[r][c], true, isDarkMode);
               this.view.highlightCell(r, c, Color.yellow);
               this.model.setNumberOfHints(--numberOfHints);
               view.showNumberOfHints("Bạn còn " + numberOfHints + " lượt gợi ý!", Color.black);
               if (numberOfHints== 0){
                   view.changeBGSuggestButton(Color.gray);
                   view.changeBGAnswerButton(Color.yellow);
               }
           }
       }
    }
    public void handleSaveGame() throws IOException {
        if (view.getGameStarted()){
            boolean isDarkMode = view.getDarkMode();
            board = model.getBoard();
           view.showMessage("Đã lưu game thành công", Color.green);
            view.setGameControlsEnabled(false);
            model.createParentDirectories("src/saveGame/sudoku_save.txt");
            model.createParentDirectories("src/saveGame/solution_save.txt");
            model.createParentDirectories("src/saveGame/fixedMatrix.txt");
            model.writeMatrixToFile("src/saveGame/sudoku_save.txt", board);
            model.writeMatrixToFile("src/saveGame/solution_save.txt", solution);
            model.writeBooleanMatrixToFile("src/saveGame/fixedMatrix.txt", fixedCells);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    view.updateCell(i, j, 0, false, isDarkMode);
                    view.setCellEditable(i, j, false);
                }
            }
            }
    }
    public void handleContinueGame() throws IOException {
        if(!view.getGameStarted()){
            changeBGWhileGameStarted();
            view.setGameControlsEnabled(true);
            view.showMessage("Hãy giải Sudoku !", Color.green);
            boolean[][] fixedBoard = model.loadContinueBoard();
            updateViewFromModel(fixedBoard);
        }
    }
    public void handleShowAnswer(){
        if (model.getNumberOfHints() == 0 && view.getGameStarted())
        {
            boolean isDarkMode = view.getDarkMode();
            int [][] solution = model.getSolution();
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
        public void  handleDarkMode(){
            if (view.getGameStarted()){
                if (!view.getDarkMode()){
                    view.setDarkMode(true);
                    view.changeBGDarkModeButton(Color.white);
                }else{
                    view.setDarkMode(false);
                    view.changeBGDarkModeButton(Color.black);
                }
                loadSudokuBoard();
            }
        }
}