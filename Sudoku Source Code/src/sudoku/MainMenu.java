package sudoku;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenu {
    
    private JButton newGame, settings, load;
    
    public JPanel createPanel(ActionListener actionListener) {
        JPanel mainMenuPanel = new JPanel(new FlowLayout());
        SudokuLayout layout = new SudokuLayout();
        mainMenuPanel.setBackground(Color.RED);
        newGame = new JButton("Play Now");
        settings = new JButton("Settings");
        load = new JButton("Load Game");
        
        newGame.addActionListener(actionListener);
        settings.addActionListener(actionListener);
        load.addActionListener(actionListener);

        
        if (layout.isSavedGameDataAvailable()) {
            mainMenuPanel.add(load);
        } else {
            mainMenuPanel.add(newGame);
        }
        mainMenuPanel.add(settings);
        
        return mainMenuPanel;
    }
    
    public JButton getNewGameButton() {
        return newGame;
    }
    
    public JButton getSettingsButton() {
        return settings;
    }
    
    public JButton getLoadButton() {
        return load;
    }
   
}