package sudoku;

public class SaveData implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public int[][] puzzle = new int[9][9];
    public int[][] completedPuzzle = new int[9][9];
    public boolean[][] editable = new boolean[9][9];
    public boolean[][] hintPosition = new boolean[9][9];
    public int minutes;
    public int seconds;
    public int hintCounter;
    public String mode;
}
