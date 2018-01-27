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

public class MaskNumber extends PlainDocument {

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        try {
            int v = Integer.parseInt(str.substring(0, 1));
        } catch (Exception e) {
            return;
        }
        super.insertString(offs, str, a); //To change body of generated methods, choose Tools | Templates.
    }
}