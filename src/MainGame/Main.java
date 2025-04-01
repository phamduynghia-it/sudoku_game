package MainGame;

import controller.SudokuController;
import model.SudokuModel;
import view.SudokuView;

public class Main {
    public static void main(String[] args) {
        SudokuModel model = new SudokuModel();
        SudokuView view = new SudokuView();
        SudokuController controller = new SudokuController(model, view);
        view.setController(controller);
        view.setVisible(true);
    }
}