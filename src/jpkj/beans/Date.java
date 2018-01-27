/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpkj.beans;

import java.io.Serializable;
import javax.swing.JTextField;
import javax.swing.text.Document;
import jpkj.MaskDate;

public class Date extends JTextField implements Serializable {


    public Date() {
        this.setDocument(new MaskDate());
    }
//    @Override
//    public Document getDocument() {
//        return new MaskDate();
//    }
}
