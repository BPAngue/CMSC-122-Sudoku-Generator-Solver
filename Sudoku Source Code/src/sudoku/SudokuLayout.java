package sudoku;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class SudokuLayout implements ActionListener {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel game;
    private JPanel mainMenu;
    private JButton generateNewPuzzle;
    private JButton back;
    private JButton solvePuzzle;
    private JButton checkInput;
    private JButton hint;
    private JButton save;
    private JLabel timerLabel;
    private JLabel hintUsed;
    private JLabel mode;
    private boolean editableStatus[][];
    private boolean hintStatus[][];
    private ChooseDifficulty chooseDiffPanel;
    private MainMenu mainMenuPanel;
    private Settings settingsPanel;
    private SudokuGenerator generate;
    private CongratulationsPanel congratulate;
    private final int SIZE = 9;
    private final int EASY_MODE = 50;
    private final int MEDIUM_MODE = 40;
    private final int HARD_MODE = 30;
    private int seconds = 0;
    private int minutes = 0;
    private static JTextField[][] textFields;
    private int[][] puzzle;
    private int[][] completedPuzzle;
    private Timer colorTimer;
    private int hintCounter = 0;
    private Timer timer;

    public void initUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        textFields = new JTextField[SIZE][SIZE];
        JPanel gamePanel = createSudoku(EASY_MODE);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainMenuPanel = new MainMenu();
        settingsPanel = new Settings();
        chooseDiffPanel = new ChooseDifficulty();
        congratulate = new CongratulationsPanel();

        mainMenu = mainMenuPanel.createPanel(this);

        mainPanel.add(mainMenu, "Main Menu");
        mainPanel.add(chooseDiffPanel.createPanel(this), "Choose Difficulty");
        mainPanel.add(settingsPanel.createPanel(this), "Settings");
        mainPanel.add(congratulate.createPanel(this), "Congratulations");
        mainPanel.add(gamePanel, "Game");

        frame.add(mainPanel);
        frame.setVisible(true);

        colorTimer = new Timer(1000, e -> revertTextColors());
        colorTimer.setRepeats(false);
    }

    public JPanel createSudoku(int gameMode) {
        createSudokuPuzzle(gameMode);
        game = new JPanel(new GridLayout(5, 3));
        generateNewPuzzle = new JButton("New Game");
        generateNewPuzzle.addActionListener(this);
        back = new JButton("Back To Main Menu");
        back.addActionListener(this);
        solvePuzzle = new JButton("Solve Puzzle");
        solvePuzzle.addActionListener(this);
        checkInput = new JButton("Check");
        checkInput.addActionListener(this);
        hint = new JButton("Hint");
        hint.addActionListener(this);
        save = new JButton("Save Game");
        save.addActionListener(this);

        timerLabel = new JLabel("Timer: 00:00");

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                }

                String time = String.format("%02d:%02d", minutes, seconds);
                timerLabel.setText("Timer: " + time);
            }
        });

        hintUsed = new JLabel("Hints Remaining: " + (3 - hintCounter));
        System.out.println(hintCounter);
        mode = new JLabel("Game Mode");

        JPanel labelPanel1 = new JPanel();
        JPanel labelPanel2 = new JPanel();
        JPanel labelPanel3 = new JPanel();

        game.add(labelPanel1);
        game.add(labelPanel2);
        game.add(labelPanel3);

        labelPanel1.add(mode);
        labelPanel2.add(timerLabel);
        labelPanel3.add(hintUsed);

        for (int subGridRow = 0; subGridRow < 3; subGridRow++) {
            for (int subGridColumn = 0; subGridColumn < 3; subGridColumn++) {
                JPanel subGrid = createSudokuSubGrid(subGridRow, subGridColumn);
                game.add(subGrid);
            }
        }

        JPanel buttonsPanel1 = new JPanel();
        JPanel buttonsPanel2 = new JPanel();
        JPanel buttonsPanel3 = new JPanel();

        game.add(buttonsPanel1);
        game.add(buttonsPanel2);
        game.add(buttonsPanel3);

        buttonsPanel1.add(generateNewPuzzle);
        buttonsPanel1.add(checkInput);
        buttonsPanel1.add(save);
        buttonsPanel2.add(back);
        buttonsPanel3.add(hint);
        buttonsPanel3.add(solvePuzzle);

        return game;
    }

    public void createSudokuPuzzle(int gameMode) {
        generate = new SudokuGenerator();
        generate.generatePuzzle(gameMode);
        puzzle = generate.getPuzzle();
        completedPuzzle = generate.getCompletedPuzzle();
    }

    public JPanel createSudokuSubGrid(int row, int column) {
        JPanel subGrid = new JPanel(new GridLayout(3, 3));
        subGrid.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JTextField textField = setTextFieldProperties();

                //Calculate actual row and column in the Sudoku grid
                int actualRow = row * 3 + i;
                int actualColumn = column * 3 + j;

                //set the text of the text field to get corresponding values in the puzzle
                textField.setText(String.valueOf(puzzle[actualRow][actualColumn]));
                if (puzzle[actualRow][actualColumn] != 0) {
                    textField.setEditable(false);
                }
                textField.setFont(textField.getFont().deriveFont(18f));
                subGrid.add(textField);
                textFields[actualRow][actualColumn] = textField;
            }
        }
        return subGrid;
    }

    private JTextField setTextFieldProperties() {
        JTextField textField = new JTextField(2);
        textField.setDocument(new TextFieldProperties());
        textField.setHorizontalAlignment(JTextField.CENTER);
        return textField;
    }

    //debug (for deletion)
    public void print() {
        for (int rows = 0; rows < SIZE; rows++) {
            for (int columns = 0; columns < SIZE; columns++) {
                System.out.print(puzzle[rows][columns] + " ");
            }
            System.out.println();
        }
    }

    //debud (for deletion)
    public void printCompletedPuzzle() {
        for (int rows = 0; rows < SIZE; rows++) {
            for (int columns = 0; columns < SIZE; columns++) {
                System.out.print(completedPuzzle[rows][columns] + " ");
            }
            System.out.println();
        }
    }

    private void updateGamePanel(int gameMode) {
        if (timer.isRunning()) {
            timer.stop();
        }

        JPanel newGamePanel = createSudoku(gameMode);
        mainPanel.remove(game); //remove existing gamePanel
        game = newGamePanel; //update the reference to the new game panel
        mainPanel.add(game, "Game"); //add the updated game Panel 
        mainPanel.revalidate(); //refresh the layout
        mainPanel.repaint(); //refresh the frame

        seconds = 0;
        minutes = 0;
        timer.start();

        hintCounter = 0;
        hint.setEnabled(true);

        hintUsed.setText("Hints Remaining: " + (3 - hintCounter));
        //debug (for deletion)
        String text = null;
        switch (gameMode) {
            case 50 ->
                text = "Easy Game";
            case 40 ->
                text = "Medium Game";
            case 30 ->
                text = "Hard Game";
            default -> {
            }
        }

        System.out.println(text);
        print();
        System.out.println();
        printCompletedPuzzle();
    }

    public void solvePuzzle() {
        hint.setEnabled(false);
        Timer solveTimer = new Timer(100, new ActionListener() {
            int row = 0;
            int col = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (row < SIZE) {
                    if (col < SIZE) {
                        if (textFields[row][col].isEditable()) {
                            if (textFields[row][col].getText().trim().isEmpty() || !textFields[row][col].getText().equals(String.valueOf(completedPuzzle[row][col]))) {
                                textFields[row][col].setForeground(Color.MAGENTA);
                                puzzle[row][col] = completedPuzzle[row][col];
                                textFields[row][col].setText(String.valueOf(completedPuzzle[row][col]));
                            }
                        }
                        col++;
                    } else {
                        col = 0;
                        row++;
                    }
                } else {
                    // Stop the timer after displaying all values
                    ((Timer) e.getSource()).stop();
                    showCongratulationsPanel();
                }
            }
        });

        solveTimer.setRepeats(true);
        solveTimer.start();
    }

    private void showCongratulationsPanel() {
        if (isPuzzleCompleted()) {
            timer.stop();
            congratulate.displayCompletedPuzzle(completedPuzzle);
            congratulate.setTime(minutes, seconds);
            hintCounter = 0;

            deleteSavedGameData();

            cardLayout.show(mainPanel, "Congratulations");
        } else {
            cardLayout.show(mainPanel, "Game");
        }
    }

    public void checkUserInput() {
        for (int rows = 0; rows < SIZE; rows++) {
            for (int columns = 0; columns < SIZE; columns++) {
                if (!textFields[rows][columns].getText().trim().isEmpty() && textFields[rows][columns].isEditable()) {
                    if (!textFields[rows][columns].getText().equals(String.valueOf(completedPuzzle[rows][columns]))) {
                        textFields[rows][columns].setForeground(Color.RED);
                        colorTimer.restart();
                    }
                }
            }
        }
    }

    public boolean isPuzzleCompleted() {
        for (int rows = 0; rows < SIZE; rows++) {
            for (int columns = 0; columns < SIZE; columns++) {
                if (textFields[rows][columns].isEditable()) {
                    String userInput = textFields[rows][columns].getText().trim();
                    String solutionValue = String.valueOf(completedPuzzle[rows][columns]);
                    if (!userInput.equals(solutionValue)) {
                        return false; // Puzzle is not completed
                    }
                }
            }
        }
        return true; // Puzzle is completed
    }

    public void revertTextColors() {
        for (int rows = 0; rows < SIZE; rows++) {
            for (int columns = 0; columns < SIZE; columns++) {
                if (textFields[rows][columns].isEditable()) {
                    textFields[rows][columns].setForeground(Color.BLACK);
                }
            }
        }
    }

    public void provideHint() {
        if (hintCounter < 3) {
            Random random = new Random();
            int row, column;

            do {
                row = random.nextInt(SIZE);
                column = random.nextInt(SIZE);
            } while (!textFields[row][column].isEditable() || !textFields[row][column].getText().trim().isEmpty());

            textFields[row][column].setText(String.valueOf(completedPuzzle[row][column]));
            textFields[row][column].setBackground(Color.LIGHT_GRAY);
            textFields[row][column].setEditable(false);

            hintCounter++;
            hintUsed.setText("Hints Remaining: " + (3 - hintCounter));

            if (hintCounter == 3) {
                hint.setEnabled(false);
            }
        }
    }

    public void saveGame() {
        SaveData data = new SaveData();

        //save current puzzle to puzzle array, editable status, and hints
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String text = textFields[row][col].getText();
                if (!text.isEmpty()) {
                    puzzle[row][col] = Integer.parseInt(text);
                } else {
                    puzzle[row][col] = 0;
                }

            }
        }

        //save editable status and hints
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                data.editable[row][col] = textFields[row][col].isEditable();
                if (textFields[row][col].getBackground() == Color.LIGHT_GRAY) {
                    data.hintPosition[row][col] = true;
                }
            }
        }

        //save puzzle state
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(puzzle[row], 0, data.puzzle[row], 0, SIZE);
        }

        //save completed puzzle state
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(completedPuzzle[row], 0, data.completedPuzzle[row], 0, SIZE);
        }

        //save timer data
        data.minutes = minutes;
        data.seconds = seconds;

        //save hint data
        data.hintCounter = hintCounter;

        //save game mode
        data.mode = mode.getText();

        try {
            ResourceManager.save(data, "sudoku_save.save");
            System.out.println("Game Saved Successfully!");
        } catch (Exception e) {
            System.out.println("Couldn't save game data: " + e.getMessage());
        }
    }

    public void loadGame() {
        try {
            SaveData data = (SaveData) ResourceManager.load("sudoku_save.save");

            // Load puzzle and completedPuzzle data
            puzzle = data.puzzle;
            completedPuzzle = data.completedPuzzle;

            // Load timer data
            minutes = data.minutes;
            seconds = data.seconds;

            // Load hint data
            hintCounter = data.hintCounter;
            hintUsed.setText("Hints Remaining: " + (3 - hintCounter));

            // Load game mode data
            mode.setText(data.mode);
            editableStatus = data.editable;
            hintStatus = data.hintPosition;

            // Update text fields based on loaded puzzle
            updateTextFields();

            // Show the loaded game panel
            cardLayout.show(mainPanel, "Game");

            // Start the timer if the game is in progress
            if (!isPuzzleCompleted()) {
                timer.start();
            }
            System.out.println();
            print();
            System.out.println();
            printCompletedPuzzle();
            System.out.println("Game Loaded Successfully!");
        } catch (Exception e) {
            System.out.println("Couldn't load game data: " + e.getMessage());
        }
    }

    private void updateTextFields() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JTextField textField = textFields[row][col];
                textField.setText(String.valueOf(puzzle[row][col]));
                textField.setEditable(editableStatus[row][col]);
                if (hintStatus[row][col] == true) {
                    textField.setBackground(Color.LIGHT_GRAY);
                }
            }
        }
    }

    public boolean isSavedGameDataAvailable() {
        Path saveFilePath = Paths.get("sudoku_save.save");
        return Files.exists(saveFilePath);
    }

    public void deleteSavedGameData() {
        try {
            Files.deleteIfExists(Paths.get("sudoku_save.save"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMainMenuPanel() {
        JPanel newMainMenuPanel = mainMenuPanel.createPanel(this);
        mainPanel.remove(mainMenu); //remove existing gamePanel
        mainMenu = newMainMenuPanel; //update the reference to the new game panel
        mainPanel.add(mainMenu, "Main Menu"); //add the updated game Panel 
        mainPanel.revalidate(); //refresh the layout
        mainPanel.repaint(); //refresh the frame
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainMenuPanel.getNewGameButton()) {
            cardLayout.show(mainPanel, "Choose Difficulty");
        } else if (e.getSource() == mainMenuPanel.getSettingsButton()) {
            cardLayout.show(mainPanel, "Settings");
        } else if (e.getSource() == settingsPanel.getBackButton()) {
            cardLayout.show(mainPanel, "Main Menu");
        } else if (e.getSource() == chooseDiffPanel.getEasyButton()) {
            updateGamePanel(EASY_MODE);
            mode.setText("EASY MODE");
            cardLayout.show(mainPanel, "Game");
        } else if (e.getSource() == chooseDiffPanel.getMediumButton()) {
            updateGamePanel(MEDIUM_MODE);
            mode.setText("MEDIUM MODE");
            cardLayout.show(mainPanel, "Game");
        } else if (e.getSource() == chooseDiffPanel.getHardButton()) {
            updateGamePanel(HARD_MODE);
            mode.setText("HARD MODE");
            cardLayout.show(mainPanel, "Game");
        } else if (e.getSource() == chooseDiffPanel.getBackButton()) {
            cardLayout.show(mainPanel, "Main Menu");
        } else if (e.getSource() == generateNewPuzzle) {
            deleteSavedGameData();
            cardLayout.show(mainPanel, "Choose Difficulty");
        } else if (e.getSource() == back) {
            updateMainMenuPanel();
            cardLayout.show(mainPanel, "Main Menu");
        } else if (e.getSource() == solvePuzzle) {
            solvePuzzle();
        } else if (e.getSource() == checkInput) {
            checkUserInput();
            if (isPuzzleCompleted()) {
                timer.stop();
                congratulate.displayCompletedPuzzle(completedPuzzle);
                congratulate.setTime(minutes, seconds);
                hintCounter = 0;
                cardLayout.show(mainPanel, "Congratulations");
            } else {
                cardLayout.show(mainPanel, "Game");
            }
        } else if (e.getSource() == hint) {
            provideHint();
            cardLayout.show(mainPanel, "Game");
        } else if (e.getSource() == congratulate.getBackToMain()) {
            updateMainMenuPanel();
            cardLayout.show(mainPanel, "Main Menu");
        } else if (e.getSource() == save) {
            saveGame();
        } else if (e.getSource() == mainMenuPanel.getLoadButton()) {
            loadGame();
        }
    }
}
