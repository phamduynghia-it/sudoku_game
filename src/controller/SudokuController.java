package controller;
import view.SudokuView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuController implements ActionListener {
    private SudokuView sudokuView;
  public  SudokuController( SudokuView sudokuView){
        this.sudokuView= sudokuView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();
        if(src.equals("<html>Chơi<br>mới</html>"))
        {
            if(sudokuView.getGameStarted() == false)
            {
                sudokuView.loadSudokuBoard();
                sudokuView.setGameStarted(true);
            }

        }else if (src.equals("Đáp án")){
            if(sudokuView.getGameStarted() == true)
            {
                sudokuView.showSudokuSolution();
            }
        }else if(src.equals("Kiểm tra")){
            if(sudokuView.getGameStarted() == true)
            {
                sudokuView.ValidBoard();
                sudokuView.checkEndGame();
            }
        }else if(src.equals("Gợi ý")){
            if(sudokuView.getGameStarted() == true)
            {
                sudokuView.Suggest();
            }
        }else if(src.equals("<html>Lưu<br>Game</html>")){
            if(sudokuView.getGameStarted() == true && sudokuView.checkEndGame() == false)
            {
                sudokuView.saveGame();
            }
        }else{
            if(sudokuView.getGameStarted() == false)
            {
                sudokuView.continueGame();
            }
        }
    }
}
