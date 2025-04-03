package view;
import controller.SudokuController;
import java.awt.Color;

public interface ISudokuView {
        void setTextDarkModeButton(String s, Color a);
        void changeBGDarkModeButton(Color color);
        void updateCell(int row, int col, int value, boolean isFixed, boolean darkMode);
        void showMessage(String message, Color color);
        void showNumberOfHints(String message, Color color);
        void setCellEditable(int row, int col, boolean editable);
        void highlightCell(int row, int col, Color color);
        void setGameControlsEnabled(boolean enabled);
        void setController(SudokuController controller);
        String getTextFromCell(int row, int col);
        void changeBGStartButton(Color color);
        void changeBGCheckButton(Color color);
        void changeBGSuggestButton(Color color);
        void changeBGAnswerButton(Color color);
        void changeBGSaveButton(Color color);
        boolean getGameStarted();
        boolean getDarkMode();
        void setDarkMode(boolean isDarkMode);
        void flashCellBorder(int row, int col, Color color, int durationMillis);
}
