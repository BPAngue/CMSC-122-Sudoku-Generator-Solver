package sudoku;

import java.util.Random;

public class SudokuGenerator {

    private final int SIZE = 9;
    private int completedPuzzle[][] = new int[SIZE][SIZE];
    private int puzzle[][] = new int[SIZE][SIZE];

    public void generatePuzzle(int emptyCells) {
        fillGrid();
        solvePuzzle(0, 0, puzzle);
        copyCompletedPuzzle();
        removeNumbers(emptyCells);
    }
    
    public int[][] getCompletedPuzzle() {
        return completedPuzzle;
    }
    
    public int[][] getPuzzle() {
        return puzzle;
    }

    public void fillGrid() {
        //Initialize the grid with zeroes
        for (int rows = 0; rows < SIZE; rows++) {
            for (int columns = 0; columns < SIZE; columns++) {
                puzzle[rows][columns] = 0;
            }
        }

        //Fill each 3x3 box with a random permutation of numbers 1 to 9
        Random random = new Random();
        for (int number = 1; number <= SIZE; number++) {
            for (int box = 0; box < SIZE; box += 3) {
                int row = box + random.nextInt(3);
                int col = box + random.nextInt(3);
                while (puzzle[row][col] != 0) {
                    row = box + random.nextInt(3);
                    col = box + random.nextInt(3);
                }
                puzzle[row][col] = number;
            }
        }

    }

    public boolean solvePuzzle(int row, int column, int[][] puzzle) {
        if (row == SIZE - 1 && column == SIZE) {
            return true;
        }

        if (column == SIZE) {
            row++;
            column = 0;
        }

        if (puzzle[row][column] != 0) {
            return solvePuzzle(row, column + 1, puzzle);
        }

        for (int number = 1; number <= SIZE; number++) {
            if (isValid(row, column, number, puzzle)) {
                puzzle[row][column] = number;
                if (solvePuzzle(row, column + 1, puzzle)) {
                    return true;
                }
                puzzle[row][column] = 0; //backtrack if a solution is not found
            }
        }
        return false;
    }

    public boolean isValid(int row, int column, int number, int[][] puzzle) {
        //Checks if current number is not present in row, column, or 3 x 3 box
        return !isNumberInRow(row, number, puzzle) && !isNumberInColumn(column, number, puzzle) && !isNumberInBox(row, column, number, puzzle);
    }

    public boolean isNumberInRow(int row, int number, int[][] puzzle) {
        for (int column = 0; column < SIZE; column++) {
            if (puzzle[row][column] == number) {
                return true;
            }
        }
        return false;
    }

    public boolean isNumberInColumn(int column, int number, int[][] puzzle) {
        for (int row = 0; row < SIZE; row++) {
            if (puzzle[row][column] == number) {
                return true;
            }
        }
        return false;
    }

    public boolean isNumberInBox(int row, int column, int number, int[][] puzzle) {
        int boxStartRow = row - row % 3;
        int boxStartColumn = column - column % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (puzzle[boxStartRow + i][boxStartColumn + j] == number) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void copyCompletedPuzzle() {
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(puzzle[i], 0, completedPuzzle[i], 0, SIZE);
        }
    }

    public void removeNumbers(int emptyCells) {
        Random random = new Random();
        int remainingCells = SIZE * SIZE - emptyCells;

        while (remainingCells > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);

            if (puzzle[row][col] != 0) {
                int temp = puzzle[row][col];
                puzzle[row][col] = 0;

                //check to see if the puzzle still has a unique solution
                if (!hasUniqueSolution(copyGrid())) {
                    puzzle[row][col] = temp; // if not, revert to the original number
                } else {
                    remainingCells--;
                }
            }
        }
    }

    public boolean hasUniqueSolution(int[][] puzzle) {
        return countSolutions(copyGrid(), 0, 0) == 1;
    }

    public int countSolutions(int[][] puzzle, int row, int col) {
        if (row == SIZE - 1 && col == SIZE) {
            return 1; // Found a solution
        }

        if (col == SIZE) {
            row++;
            col = 0;
        }

        if (puzzle[row][col] != 0) {
            return countSolutions(puzzle, row, col + 1);
        }

        int count = 0;
        for (int num = 1; num <= SIZE; num++) {
            if (isValid(row, col, num, puzzle)) {
                puzzle[row][col] = num;
                count += countSolutions(puzzle, row, col + 1);
                puzzle[row][col] = 0; // Backtrack
            }
        }
        return count;
    }

    public int[][] copyGrid() {
        int[][] copyGrid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(puzzle[i], 0, copyGrid[i], 0, SIZE);
        }
        return copyGrid;
    }
}
