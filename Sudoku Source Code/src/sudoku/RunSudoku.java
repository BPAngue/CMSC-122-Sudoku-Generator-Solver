package sudoku;

public class RunSudoku {
    
    public static void main(String[] args) {
        SudokuLayout game = new SudokuLayout();
        game.initUI();
        
        //debug (for deletion)
        game.print();
        System.out.println();
        game.printCompletedPuzzle();
    }
}
