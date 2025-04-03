package view;
import controller.SudokuController;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class SudokuView extends JFrame implements ISudokuView {
    private final JTextField[][] cells = new JTextField[9][9];
    private JLabel statusLabel;
    private JLabel noticeLabel;
    private JButton startButton;
    private JButton answerButton;
    private JButton checkButton;
    private JButton suggestButton;
    private JButton saveButton;
    private JButton continueButton;
    private JPanel panel;
    private Boolean isGameStarted = false;

    public JTextField[][] getCells() {
        return cells;
    }
    public SudokuView() {
        init();
    }


    public JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(120, 50));
        return button;
    }
    private void initializeButtons() {
        startButton = createButton("<html>Chơi<br>mới</html>", Color.RED);
        checkButton = createButton("Kiểm tra", Color.LIGHT_GRAY);
        suggestButton = createButton("Gợi ý", Color.LIGHT_GRAY);
        answerButton = createButton("<html>Xem <br>đáp án</html>", Color.LIGHT_GRAY);
        saveButton = createButton("<html>Lưu<br>Game</html>", Color.RED);
        continueButton = createButton("<html>Chơi<br>tiếp<html>", Color.GREEN);
    }
    @Override
    public boolean getGameStarted() {
        return isGameStarted;
    }
    @Override
    public void changeBGStartButton(Color color){
        startButton.setBackground(color);
        startButton.setOpaque(true);
    }
    @Override
    public void changeBGCheckButton(Color color){
        checkButton.setBackground(color);
        checkButton.setOpaque(true);
    }
    @Override
    public void changeBGSuggestButton(Color color){
        suggestButton.setBackground(color);
        suggestButton.setOpaque(true);
    }
    @Override
    public void changeBGAnswerButton(Color color){
       answerButton.setBackground(color);
        answerButton.setOpaque(true);
    }
    @Override
    public void changeBGSaveButton(Color color){
        saveButton.setBackground(color);
        saveButton.setOpaque(true);
    }
    private void init() {
        this.setTitle("Sudoku Game");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 9));
        panel.setPreferredSize(new Dimension(450, 450));


        // Màu sắc cho các khối 3x3 (màu pastel nhẹ nhàng)
        Color[] blockColors = {
                new Color(255, 255, 204), // Vàng nhạt
                new Color(204, 255, 204), // Xanh lá nhạt
                new Color(204, 255, 255), // Xanh da trời nhạt
                new Color(255, 204, 255), // Hồng nhạt
                new Color(255, 204, 204), // Đỏ nhạt
                new Color(204, 204, 255), // Tím nhạt
                new Color(255, 255, 204), // Vàng nhạt (lặp lại)
                new Color(204, 255, 204), // Xanh lá nhạt (lặp lại)
                new Color(204, 255, 255)  // Xanh da trời nhạt (lặp lại)
        };

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new JTextField();


                cells[i][j].setPreferredSize(new Dimension(50, 50));
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(new Font("Arial", Font.BOLD, 24)); // Font lớn hơn


                int blockIndex = (i / 3) * 3 + (j / 3); // 0-8
                cells[i][j].setBackground(blockColors[blockIndex]);


                int topBorder = (i % 3 == 0) ? 3 : 1;
                int leftBorder = (j % 3 == 0) ? 3 : 1;
                int bottomBorder = (i % 3 == 2) ? 3 : 1;
                int rightBorder = (j % 3 == 2) ? 3 : 1;

                cells[i][j].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(topBorder, leftBorder, bottomBorder, rightBorder, Color.black),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));

                // Thiết lập màu chữ
                cells[i][j].setForeground(Color.BLACK);

                // Không cho edit ban đầu
                cells[i][j].setEditable(false);

                panel.add(cells[i][j]);
            }
        }

// Thiết lập background cho panel chính
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // tao cac nut chuc nang
        this.initializeButtons();
        JPanel jPanel_button = new JPanel();
        jPanel_button.setLayout(new GridLayout(3, 2, 5, 5));
        jPanel_button.setPreferredSize(new Dimension(200, 300));
        jPanel_button.add(checkButton);
        jPanel_button.add(startButton);
        jPanel_button.add(answerButton);
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

    }
    @Override
    public void updateCell(int row, int col, int value, boolean isFixed) {
        cells[row][col].setText(value == 0 ? "" : String.valueOf(value));
        cells[row][col].setEditable(!isFixed);
        cells[row][col].setBackground(isFixed ? Color.LIGHT_GRAY : Color.WHITE);
    }

    @Override
    public void showMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
    @Override
    public void showNumberOfHints(String message, Color color) {
        noticeLabel.setText(message);
        noticeLabel.setForeground(color);
    }
    @Override
   public void setCellEditable(int row, int col, boolean editable){
        cells[row][col].setEditable(editable);
    }
    @Override
    public void highlightCell(int row, int col, Color color){
        cells[row][col].setBackground(color);
        cells[row][col].setOpaque(true);
    }
    @Override
    public void setGameControlsEnabled(boolean enabled){
        isGameStarted = enabled;
    }
    @Override
    public void setController(SudokuController controller) {
        startButton.addActionListener(e -> controller.handleNewGame());
        checkButton.addActionListener(e -> controller.handleCheckGame());
        suggestButton.addActionListener(e -> controller.handleHint());
        answerButton.addActionListener(e -> controller.handleShowAnswer());
        saveButton.addActionListener(e -> {
            try {
                controller.handleSaveGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        continueButton.addActionListener(e -> {
            try {
                controller.handleContinueGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    @Override
    public String getTextFromCell(int row, int col){
        return cells[row][col].getText();
    }
}








