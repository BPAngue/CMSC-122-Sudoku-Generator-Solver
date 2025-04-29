package sudoku;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TextFieldProperties extends PlainDocument {
    
   @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        String currentText = getText(0, getLength());
        String newText = currentText.substring(0, offset) + str + currentText.substring(offset);

        if (isValidInput(newText)) {
            super.insertString(offset, str, attr);
        }
    }

    private boolean isValidInput(String text) {
        return text.matches("[1-9]") && text.length() <= 1;
    }
    
    @Override
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
    }
}
