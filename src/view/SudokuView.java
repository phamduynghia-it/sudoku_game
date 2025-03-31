package view;

import controller.SudokuController;
import model.SudokuModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class SudokuView extends JFrame {
    private SudokuModel sudokuModel;
    private final JTextField[][] cells = new JTextField[9][9];
    private JLabel statusLabel;
    private JLabel noticeLabel;
    private JButton startButton;
    private JButton newLevelButton;
    private JButton checkButton;
    private JButton suggestButton;
    private JButton saveButton;
    private JButton continueButton;
    private JPanel panel;
    private Boolean isGameStarted = false;
    private boolean[][] isFixedValue;
    private int[][] solution ;
    private int[][] board;
    private  int numberOfHints = 5;

    public int getNumberOfHints() {
        return numberOfHints;
    }
    public void setButtonWhileGameStart()
    {
        this.setBackgroundButton(startButton, Color.gray);
        this.setBackgroundButton(checkButton, Color.GREEN);
        this.setBackgroundButton(suggestButton, Color.YELLOW);
    }
    public SudokuView() {
        init();
    }
    public Boolean getGameStarted() {
        return isGameStarted;
    }
    public void setGameStarted(Boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public void setBackgroundButton(JButton jButton, Color color)
    {
        jButton.setBackground(color);
        jButton.setOpaque(true);
    }
    public void setTextLabel(JLabel jLabel, String s)
    {
        jLabel.setText(s);
    }
    public JButton createButton(String text, Color color, ActionListener ac) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setOpaque(true);
        button.addActionListener(ac);
        button.setPreferredSize(new Dimension(120, 50));
        return button;
    }

    private void init() {
        this.setTitle("Sudoku Game");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        ActionListener ac = new SudokuController(this);

        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 9));
        panel.setPreferredSize(new Dimension(450, 450));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new JTextField();
                cells[i][j].setPreferredSize(new Dimension(50, 50));
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                cells[i][j].setEditable(false);
                panel.add(cells[i][j]);
            }
        }

        // tao cac nut chuc nang
        startButton = createButton("<html>Chơi<br>mới</html>", Color.RED, ac);
        checkButton = createButton("Kiểm tra", Color.LIGHT_GRAY, ac);
        suggestButton = createButton("Gợi ý", Color.LIGHT_GRAY, ac);
        newLevelButton = createButton("<html>Đổi <br>màn</html>", Color.LIGHT_GRAY, ac);
        saveButton= createButton("<html>Lưu<br>Game</html>", Color.RED, ac);
        continueButton= createButton("<html>Chơi<br>tiếp<html>", Color.green, ac);


        JPanel jPanel_button = new JPanel();
        jPanel_button.setLayout(new GridLayout(3, 2, 5, 5));
        jPanel_button.setPreferredSize(new Dimension(200, 300));
        jPanel_button.add(checkButton);
        jPanel_button.add(startButton);
        jPanel_button.add(newLevelButton);
        jPanel_button.add(suggestButton);
        jPanel_button.add(saveButton);
        jPanel_button.add(continueButton);


        statusLabel = new JLabel("Hãy giải Sudoku!", SwingConstants.CENTER);
        statusLabel.setPreferredSize(new Dimension(800, 40));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(Color.BLUE);


        noticeLabel = new JLabel("", SwingConstants.CENTER);
        noticeLabel.setPreferredSize(new Dimension(800, 40));
        noticeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        noticeLabel.setForeground(Color.RED);


        this.setLayout(new BorderLayout());
        this.add(statusLabel, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);
        this.add(jPanel_button, BorderLayout.EAST);
        this.add(noticeLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // load ma tran len man hinh
    public void loadSudokuBoard() {
       this.setButtonWhileGameStart();
       this.setBackgroundButton(continueButton, Color.gray);
        numberOfHints = 5;
        sudokuModel = new SudokuModel();
        isFixedValue = this.sudokuModel.getFixedCells();
        solution = this.sudokuModel.getSolution();
        board = this.sudokuModel.getBoard();
        noticeLabel.setText("Bạn còn " + this.getNumberOfHints() + " lượt gợi ý !");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                if (board[i][j] != 0) {
                    cells[i][j].setText((board[i][j])+"");
                    cells[i][j].setEditable(false);
                    cells[i][j].setBackground(Color.LIGHT_GRAY);
                } else {
                    cells[i][j].setText("");
                    cells[i][j].setEditable(true);
                }
            }
        }
    }
    public void loadSudokuBoard(int numberOfHints) {
        this.numberOfHints = numberOfHints;
        noticeLabel.setText("Bạn còn " + this.getNumberOfHints() + " lượt gợi ý !");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    cells[i][j].setText((board[i][j])+"");
                    if (isFixedValue[i][j])
                    {
                        cells[i][j].setEditable(false);
                        cells[i][j].setBackground(Color.LIGHT_GRAY);
                    }else {
                        cells[i][j].setEditable(true);
                    }
                } else {
                    cells[i][j].setText(""); // Ô trống
                    cells[i][j].setEditable(true);
                }
            }
        }
    }
    public void ValidBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!isFixedValue[i][j]) {
                    String input = cells[i][j].getText().trim();
                    if (!input.isEmpty()) {
                        try {
                            int temp = Integer.parseInt(input);

                            if (sudokuModel.ValidMatrix(i, j, temp) && temp < 10 && temp >0) {
                                cells[i][j].setBackground(Color.GREEN);
                                sudokuModel.setBoard(i, j, temp);
                            } else {
                                cells[i][j].setBackground(Color.RED);
                            }
                        } catch (NumberFormatException e) {
                            cells[i][j].setBackground(Color.RED);
                        }
                    } else {
                        cells[i][j].setBackground(Color.WHITE);
                        sudokuModel.setBoard(i, j, 0);
                    }
                }
            }
        }
    }
    public void Suggest() {
        if(numberOfHints == 0){
            this.setBackgroundButton(suggestButton, Color.gray);
            return;
        }else {
            Random rand = new Random();
            int r, c;
            do {
                r = rand.nextInt(9);
                c = rand.nextInt(9);
            } while (isFixedValue[r][c]);

            this.sudokuModel.setBoard(r, c, solution[r][c]);
            this.isFixedValue[r][c] = true;
            this.sudokuModel.setIsFixedCell(r, c ,true);

            cells[r][c].setText(solution[r][c] + "");
            cells[r][c].setEditable(false);
            cells[r][c].setBackground(Color.YELLOW);
            numberOfHints -- ;
            noticeLabel.setText("Bạn còn " + this.getNumberOfHints() + " lượt gợi ý !");
            if(numberOfHints == 0) {
                this.setBackgroundButton(suggestButton, Color.gray);
            }
        }
    }
    public Boolean checkEndGame() {
        if (this.sudokuModel.endGame() == true)
        {
            this.setGameStarted(false);
            this.setTextLabel(statusLabel,"Chúc mừng! Bạn đã chiến thắng!" );
            statusLabel.setForeground(Color.GREEN);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    cells[i][j].setEditable(false);
                    cells[i][j].setBackground(Color.green);
                }
            }
            return true;
        }
            this.setTextLabel(statusLabel,"Chưa hoàn thành, hãy thử lại!");
            statusLabel.setForeground(Color.RED);
            return false;
    }
    public void saveGame(){
        if (isGameStarted)
        {
            this.setTextLabel(statusLabel, "Đã lưu game thành công");
            isGameStarted= false;
            sudokuModel.saveGameToFile("src/saveGame/sudoku_save.txt","src/saveGame/fixedMatrix.txt" );
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    cells[i][j].setEditable(false);
                    cells[i][j].setText("");
                    cells[i][j].setBackground(Color.white);
                }
            }
            this.setBackgroundButton(continueButton, Color.green);
        }
    }
    public void continueGame()
    {
        this.setButtonWhileGameStart();
        if (this.sudokuModel == null) {
            this.sudokuModel = new SudokuModel();
        }
        board = SudokuModel.readSinglePuzzleFromFile("src/saveGame/sudoku_save.txt");
        isFixedValue = SudokuModel.readBooleanMatrixFromFile("src/saveGame/fixedMatrix.txt");
        if (board != null && isFixedValue != null) {
            this.setGameStarted(true);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    sudokuModel.setIsFixedCell(i, j, isFixedValue[i][j]);
                    if (isFixedValue[i][j]) {
                        sudokuModel.setBoard(i, j , board[i][j]);
                    } else {
                        sudokuModel.setBoard(i, j , 0);
                    }
                }
            }
            loadSudokuBoard(5);
            this.setBackgroundButton(continueButton, Color.gray);
        } else {
            System.out.println("Không thể tiếp tục game: Dữ liệu bị lỗi hoặc không tồn tại.");
        }
    }
    public void loadSudokuBoard(boolean b) {
        this.setButtonWhileGameStart();
        this.setBackgroundButton(continueButton, Color.gray);
        numberOfHints = 5;
        sudokuModel.loadRandomBoard();
        solution = this.sudokuModel.getSolution();
        board = this.sudokuModel.getBoard();
        isFixedValue = this.sudokuModel.getFixedCells();
        noticeLabel.setText("Bạn còn " + this.getNumberOfHints() + " lượt gợi ý !");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    cells[i][j].setText((board[i][j])+"");
                    cells[i][j].setEditable(false);
                    cells[i][j].setBackground(Color.LIGHT_GRAY);
                } else {
                    cells[i][j].setText("");
                    cells[i][j].setEditable(true);
                    cells[i][j].setBackground(Color.white);
                }
            }
        }
    }
    }







