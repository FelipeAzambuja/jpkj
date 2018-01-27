/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpkj;

/**
 *
 * @author felipe
 */
public class Entry {

    /**
     * Abre uma janela para entrada de texto.
     *
     * @param titulo titulo que será aparecido
     * @return
     */
    public static String show(String titulo) {
        return javax.swing.JOptionPane.showInputDialog(titulo);
    }

    public static int showInt(String titulo) {
        Integer retorno = 0;
        boolean ok = true;
        do {
            String tmp = javax.swing.JOptionPane.showInputDialog(titulo).trim();
            try {
                retorno = Integer.parseInt(tmp);
                ok = true;
            } catch (Exception e) {
                ok = false;
            } finally {
            }
        } while (!ok);
        return retorno;
    }

    public static double showDouble(String titulo) {
        Double retorno = 0.0;
        boolean ok = true;
        do {
            String tmp = javax.swing.JOptionPane.showInputDialog(titulo).trim();
            try {
                retorno = Double.parseDouble(tmp);
                ok = true;
            } catch (Exception e) {
                ok = false;
            } finally {
            }
        } while (!ok);
        return retorno;
    }

    public static float showFloat(String titulo) {
        Float retorno = 0.0f;
        boolean ok = true;
        do {
            String tmp = javax.swing.JOptionPane.showInputDialog(titulo).trim();
            try {
                retorno = Float.parseFloat(tmp);
                ok = true;
            } catch (Exception e) {
                ok = false;
            } finally {
            }
        } while (!ok);
        return retorno;
    }

    public static String button(String titulo, String[] ops) {
        return BTN(titulo, ops);
    }

    public static String BTN(String titulo, String[] ops) {
        String[] op = ops;
        Object ret = javax.swing.JOptionPane.showOptionDialog(null, titulo, "", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.INFORMATION_MESSAGE, null, op, op[0]);
        return op[Integer.valueOf(ret.toString())];
    }
}
