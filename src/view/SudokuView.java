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

    public JButton getCheckButton() {
        return checkButton;
    }

    public JButton getContinueButton() {
        return continueButton;
    }

    public JButton getAnswerButton(){
        return answerButton;
    }

    public JLabel getNoticeLabel() {
        return noticeLabel;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JButton getSuggestButton() {
        return suggestButton;
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

    private void init() {
        this.setTitle("Sudoku Game");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 9));
        panel.setPreferredSize(new Dimension(450, 450));


        Color[] blockColors = {
                new Color(255, 235, 205),
                new Color(220, 245, 245),
                new Color(255, 228, 225)
        };

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new JTextField();
                cells[i][j].setPreferredSize(new Dimension(50, 50));
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                cells[i][j].setEditable(false);


                int blockRow = i / 3;
                int blockCol = j / 3;
                int colorIndex = (blockRow + blockCol) % blockColors.length;
                cells[i][j].setBackground(blockColors[colorIndex]);


                cells[i][j].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));

                panel.add(cells[i][j]);
            }
        }
        panel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 1, Color.BLACK));
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








