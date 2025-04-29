package sudoku;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ChooseDifficulty {
    
    JButton easy, medium, hard, back;
    
    public JPanel createPanel(ActionListener actionListener) {
        JPanel chooseDifficultyPanel = new JPanel();
        chooseDifficultyPanel.setBackground(Color.YELLOW);
        
        easy = new JButton("Easy");
        medium = new JButton("Medium");
        hard = new JButton("Hard");
        back = new JButton("Back");
        
        easy.addActionListener(actionListener);
        medium.addActionListener(actionListener);
        hard.addActionListener(actionListener);
        back.addActionListener(actionListener);
        
        chooseDifficultyPanel.add(easy);
        chooseDifficultyPanel.add(medium);
        chooseDifficultyPanel.add(hard);
        chooseDifficultyPanel.add(back);
        
        return chooseDifficultyPanel;
    }
    
    public JButton getEasyButton() {
        return easy;
    }
    
    public JButton getMediumButton() {
        return medium;
    }
    
    public JButton getHardButton() {
        return hard;
    }
    
    public JButton getBackButton() {
        return back;
    }
}
