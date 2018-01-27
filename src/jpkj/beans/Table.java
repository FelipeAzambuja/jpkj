package jpkj.beans;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import jpkj.Swing;

public class Table extends JTable {

    public static String columnType_check = "checkbox";
    public static String columnType_combo = "combobox";
    public static String columnType_text = "textfield";
    boolean look = true;

    public void changeColumnType(int column, String type) {
        Swing.changeColumnType(this, column, type);
    }

    public void look() {
        DefaultTableModel modelo = (DefaultTableModel) this.getModel();
        this.getTableHeader().setReorderingAllowed(false);
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.setModel(modelo);
        look = true;
    }

    public void unLook() {
        DefaultTableModel modelo = (DefaultTableModel) this.getModel();
        this.getTableHeader().setReorderingAllowed(true);
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        this.setModel(modelo);
        look = false;
    }

    public boolean isLook() {
        return look;
    }

    public void clearData() {
        DefaultTableModel m = (DefaultTableModel) this.getModel();
        m.setNumRows(0);
        m.setRowCount(0);
        this.setModel(m);
    }

    public void clearColumns() {
        DefaultTableModel m = (DefaultTableModel) this.getModel();
        m.setColumnCount(0);
        this.setModel(m);
    }

    public void clearAll() {
        clearColumns();
        clearData();
    }

    public void addLine(Object[] line) {
        Swing.addLineTable(this, line);
    }

    public void addColumn(Object column) {
        Swing.addColumnTable(this, column.toString());
    }

    public void addColumns(Object[] columns) {
        for (Object c : columns) {
            this.addColumn(c);
        }
    }

    public void setData(ArrayList<HashMap<String, String>> dados) {
        clearData();
        look();
        clearColumns();
        addColumns(dados.get(0).keySet().toArray());
        addData(dados);
    }

    public void setData(ArrayList<HashMap<String, String>> dados, String[] colunas) {
        clearData();
        look();
        clearColumns();
        addColumns(colunas);
        addData(dados);
    }

    public void removeAt(int index) {
        if (index > -1) {
            DefaultTableModel modelo = (DefaultTableModel) this.getModel();
            modelo.removeRow(index);
            this.setModel(modelo);
        }
    }

    public void addData(ArrayList<HashMap<String, String>> dados) {
        for (HashMap<String, String> d : dados) {
            ArrayList<String> linha = new ArrayList<String>();
            for (String i : dados.get(0).keySet()) {
                linha.add(d.get(i));
            }
            this.addLine(linha.toArray());
        }
    }
}
