package jpkj.beans;

import javax.swing.JTextField;
import javax.swing.text.Document;
import jpkj.MaskNumber;

public class Number extends JTextField {

    public void setValue( int valor ) {
        this.setText( valor + "" );
    }

    public int getValue() {
        int retorno = 0;
        if ( !this.getText().trim().isEmpty() ) {
            retorno = Integer.parseInt( this.getText() );
        }
        return retorno;
    }

    public Number() {
        this.setDocument( new MaskNumber() );
    }
}
