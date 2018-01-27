/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpkj.beans;

import java.util.regex.Pattern;
import javax.swing.JTextField;
import jpkj.MaskMoney;

public class Money extends JTextField {

    public void setValue( String valor ) {
        setValue( Float.parseFloat( valor.trim() ) );
    }

    public void setValue( Float valor ) {
        String tmp = valor + "";
        this.setDocument( new MaskMoney() );
        String tmp2 = tmp.substring( tmp.indexOf( "." ) + 1 );
        int tmp3 = tmp2.length();
        int tmp4 = 2 - tmp3;
        for ( int i = 0 ; i < tmp4 ; i++ ) {
            tmp += "0";
        }
        String tmp5 = tmp.replaceAll( Pattern.quote( "." ) , "" );
        this.setText( tmp5 );
    }

    public Float getValue() {
        Float retorno = 0.0f;
        if ( !this.getText().trim().isEmpty() ) {
            retorno = Float.parseFloat( this.getText() );
        }        
        return retorno;
    }

    public Money() {
        this.setDocument( new MaskMoney() );
    }

}
