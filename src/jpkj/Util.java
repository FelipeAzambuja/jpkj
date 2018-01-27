package jpkj;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class Util {

    public static String formatNumber(double numero) {
        String retorno = "";
        retorno = NumberFormat.getCurrencyInstance().format(numero);
        return retorno;
    }

    public static String exeptionToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage());
        sb.append("\n");
        sb.append("message : "+e.getMessage());
        sb.append("localized : "+e.getLocalizedMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String base64Decode(String s) {
        return StringUtils.newStringUtf8(Base64.decodeBase64(s));
    }

    public static String base64Encode(String s) {
        return Base64.encodeBase64String(StringUtils.getBytesUtf8(s));
    }

    public static String cmd(String comando) {
        String retorno = "";
        try {
            Process exec = Runtime.getRuntime().exec(comando);
            BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String linha = "", total = "";
            while ((linha = br.readLine()) != null) {
                total += linha + "\r\n";
            }
            retorno = new String(total.getBytes("UTF-8"));
        } catch (Exception e) {
            Log.out(Util.exeptionToString(e));
        }
        return retorno;
    }

    public static void showJFrame(JFrame janela, String title) {
        showJFrame(janela, title, false, null);
    }

    public static void showJFrame(JFrame janela, String title, File icon) {
        showJFrame(janela, title, false, icon);
    }

    public static void showJFrame(JFrame janela, String title, boolean close) {
        showJFrame(janela, title, close, null);
    }

    public static void showJFrame(JFrame janela, String title, boolean close, File icon) {
        janela.setTitle(title);
        if (!close) {
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } else {
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        if (icon != null) {
            ImageIcon imageIcon = new ImageIcon(icon.getAbsolutePath());
            janela.setIconImage(imageIcon.getImage());
        }
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }
    public static int MAC = 0;
    public static int WINDOWS = 1;
    public static int WINDOWS_CLASSIC = 2;
    public static int GTK = 3;
    public static int JAVA = 4;
    public static int NIMBUS = 5;
    public static int SISTEMA = 6;

    public static void changeTheme(LookAndFeel laf) {
        try {
            UIManager.setLookAndFeel(laf);
        } catch (Exception e) {
            Log.out(Util.exeptionToString(e));
        }
    }
    public static void changeTheme(int tema) {
        try {
            switch (tema) {
                case 0:
                    UIManager.setLookAndFeel("ch.randelshofer.quaqua.leopard.Quaqua15LeopardCrossPlatformLookAndFeel");
//     System.out.println(ch.randelshofer.quaqua.QuaquaManager.getLookAndFeelClassName());
                    break;
                case 1:
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    break;
                case 2:
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                    break;
                case 3:
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                    break;
                case 4:
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    break;
                case 5:
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                case 6:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                default:
                    System.out.println("TEMA NÂO ESPECIFICADO");
            }
        } catch (Exception e) {
            Log.out(Util.exeptionToString(e));
        } finally {
        }
    }
}
