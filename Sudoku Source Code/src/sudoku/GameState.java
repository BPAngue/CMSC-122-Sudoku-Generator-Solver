package sudoku;

import java.io.Serializable;

public class GameState implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private int[][] puzzle;
    private int[][] completedPuzzle;
    private int hintCounter;
    
    
    public GameState(int[][] puzzle, int[][] completedPuzzle, int hintCounter) {
        this.puzzle = puzzle;
        this.completedPuzzle = completedPuzzle;
        this.hintCounter = hintCounter;
    }
    
    public int[][] getPuzzle() {
        return puzzle;
    }
    
    public int[][] getCompletedPuzzle() {
        return completedPuzzle;
    }
    
    public int getHintCounter() {
        return hintCounter;
    }
}
