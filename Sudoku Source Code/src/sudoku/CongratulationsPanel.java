package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CongratulationsPanel {

    private JButton backToMain;
    private JLabel congratulate;
    private JLabel completed;
    private int minutes;
    private int seconds;
    
    public void setTime(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        congratulate.setText("Puzzle Completed in " + minutes + " minutes and " + seconds + " seconds");
    }

    public JPanel createPanel(ActionListener actionListener) {
        JPanel congratulationsPanel = new JPanel(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        JPanel centerPanel = new JPanel(new GridBagLayout());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.GREEN);
        centerPanel.setBackground(Color.YELLOW);
        
        congratulate = new JLabel("Puzzle Completed!");
        backToMain = new JButton("Back To Main Menu");
        backToMain.addActionListener(actionListener);
        completed = new JLabel();
        
        topPanel.add(congratulate);
        bottomPanel.add(backToMain);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0); // Add some padding
        centerPanel.add(completed, gbc);
    
        congratulationsPanel.add(topPanel, BorderLayout.NORTH);
        congratulationsPanel.add(centerPanel, BorderLayout.CENTER);
        congratulationsPanel.add(bottomPanel, BorderLayout.SOUTH);

        return congratulationsPanel;
    }

    public JButton getBackToMain() {
        return backToMain;
    }

    public void displayCompletedPuzzle(int[][] completedPuzzle) {
        StringBuilder html = new StringBuilder("<html><body><table border='1' style='border-collapse: collapse;'>");
        for (int i = 0; i < 9; i++) {
            html.append("<tr>");
            for (int j = 0; j < 9; j++) {
                int number = completedPuzzle[i][j];
                html.append("<td style='width: 30px; height: 30px; text-align: center; font-weight: bold;'>")
                        .append(number > 0 ? number : "")
                        .append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</table></body></html>");
        completed.setText(html.toString());
        completed.setFont(new Font("Arial", Font.PLAIN, 16));
    }
}
