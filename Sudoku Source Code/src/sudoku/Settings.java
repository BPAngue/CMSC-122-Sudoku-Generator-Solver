
package sudoku;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Settings {
    
    private JButton back;
    
    public JPanel createPanel(ActionListener actionListener) {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(Color.BLUE);
        
        back = new JButton("Back");
        back.addActionListener(actionListener);
    
        settingsPanel.add(back);
        return settingsPanel;
    }
    
    public JButton getBackButton() {
        return back;
    }
}
