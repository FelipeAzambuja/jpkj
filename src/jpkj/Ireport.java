package jpkj;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperPrint;

public class Ireport {

    public static void show(File arquivo, ResultSet rs) {
        try {
            JRResultSetDataSource jds = new JRResultSetDataSource(rs);
            JasperPrint rel = net.sf.jasperreports.engine.JasperFillManager.fillReport(arquivo.getPath(), null, jds);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new net.sf.jasperreports.view.JasperViewer(rel).getContentPane());
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        } catch (Exception e) {
            Log.out(Util.exeptionToString(e));
        }
    }

    public static void show(File arquivo, HashMap<String, Object> p,Connection c) {
        try {
            JasperPrint rel = net.sf.jasperreports.engine.JasperFillManager.fillReport(arquivo.getPath(), p, c);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new net.sf.jasperreports.view.JasperViewer(rel).getContentPane());
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        } catch (Exception e) {
            Log.out(Util.exeptionToString(e));
        }
    }
}
