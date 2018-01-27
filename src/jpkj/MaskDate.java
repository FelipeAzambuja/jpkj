/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpkj;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
/**
 *
 * @author felipe
 */

public class MaskDate extends PlainDocument {

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        String texto = getText(0, getLength());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c)) {
                return;
            }
        }
        super.remove(0, getLength());
        StringBuffer s = new StringBuffer(texto + str);
        if (offs == 2) {
            s.insert(offs, "/");
        }
        if (offs == 5) {
            s.insert(offs, "/");
        }
        if (offs > 8) {
            super.insertString(0, s.toString().substring(0, 10), a);
        } else {
            super.insertString(0, s.toString(), a);
        }
    }
}
