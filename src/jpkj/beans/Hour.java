package jpkj.beans;

import javax.swing.JTextField;
import javax.swing.text.Document;
import jpkj.MaskHour;

public class Hour extends JTextField {

    public Hour() {
        this.setDocument(new MaskHour());
    }
}
