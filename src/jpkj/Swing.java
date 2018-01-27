package jpkj;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author felipe
 */
public class Swing {

    public static void clearTable(JTable table) {
        DefaultTableModel modelo_tabela = (DefaultTableModel) table.getModel();
        modelo_tabela.setRowCount(0);
        modelo_tabela.setColumnCount(0);
        
        table.setModel(modelo_tabela);
    }

    public static void setListCombo(JComboBox combo, String[] dados) {
        DefaultComboBoxModel modelo = (DefaultComboBoxModel) combo.getModel();
        modelo.removeAllElements();
        for (String string : dados) {
            modelo.addElement(string);
        }
        combo.setModel(modelo);
    }

    public static void lookTable(JTable tabela) {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        tabela.getTableHeader().setReorderingAllowed(false);
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela.setModel(modelo);
    }

    public static String columnType_check = "checkbox";
    public static String columnType_combo = "combobox";
    public static String columnType_text = "textfield";
    // http://developer.classpath.org/doc/javax/swing/table/DefaultTableCellRenderer-source.html

    public static void changeColumnType(final JTable tabela, int indiceColuna, final String columnType) {
        tabela.getColumnModel().getColumn(indiceColuna).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component retorno = null;
                if (columnType.equalsIgnoreCase(Swing.columnType_check)) {
                    retorno = (JCheckBox) (table.getValueAt(row, column));
                } else if (columnType.equalsIgnoreCase(Swing.columnType_combo)) {
                    retorno = (JComboBox) table.getValueAt(row, column);
                } else {
                    retorno = (JTextField) table.getValueAt(row, column);
                }
                return retorno;
            }
        });
        if (columnType.equalsIgnoreCase(Swing.columnType_check)) {
            tabela.getColumnModel().getColumn(indiceColuna).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
                @Override
                public Object getCellEditorValue() {
                    JCheckBox ele = (JCheckBox) tabela.getValueAt(tabela.getSelectedRow(), tabela.getSelectedColumn());
                    ele.setSelected(!ele.isSelected());
                    return ele;
                }
            });
        } else if (columnType.equalsIgnoreCase(Swing.columnType_combo)) {
            tabela.getColumnModel().getColumn(indiceColuna).setCellEditor(new DefaultCellEditor(new JComboBox()) {
                public JComponent getEditorComponent() {
                    JComboBox ele = (JComboBox) tabela.getValueAt(tabela.getSelectedRow(), tabela.getSelectedColumn());
                    this.editorComponent = ele;
                    return ele;
                }

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    JComboBox ele = (JComboBox) table.getValueAt(row, column);
                    this.editorComponent = ele;
                    return ele;
                }

                @Override
                public Object getCellEditorValue() {
                    JComboBox ele = (JComboBox) tabela.getValueAt(tabela.getSelectedRow(), tabela.getSelectedColumn());
                    this.editorComponent = ele;
                    return ele;
                }
            });
        } else {
            tabela.getColumnModel().getColumn(indiceColuna).setCellEditor(new DefaultCellEditor(new JTextField()) {
                public JComponent getEditorComponent() {
                    JTextField ele = (JTextField) tabela.getValueAt(tabela.getSelectedRow(), tabela.getSelectedColumn());
                    this.editorComponent = ele;
                    return ele;
                }

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    JTextField ele = (JTextField) table.getValueAt(row, column);
                    this.editorComponent = ele;
                    return ele;
                }

                @Override
                public Object getCellEditorValue() {
                    JTextField ele = (JTextField) tabela.getValueAt(tabela.getSelectedRow(), tabela.getSelectedColumn());
                    this.editorComponent = ele;
                    return ele;
                }
            });
        }
    }

    public static KeyListener autoComplete(final JTextField campo, final String[] itens) {
        campo.setText("");
        itens[itens.length] = "";
        KeyListener retorno = new KeyListener() {
            String jaDigitado = "";

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                try {
                    campo.setText(campo.getText().substring(0, campo.getText().length() - 1));
                } catch (Exception e) {
                } finally {
                }
                if (ke.getKeyCode() == 8 && jaDigitado.length() > 0) {//apagar
                    jaDigitado = jaDigitado.substring(0, jaDigitado.length() - 1);//remover o ultimo
                }
                if (ke.getKeyCode() != 8 && ke.getKeyCode() != 16) {
                    jaDigitado += new String(("" + ke.getKeyChar()));//Tinha um motivo não lembro qual
                }
                boolean passou = false;
                String digitado = campo.getText();//Sei lá mil tretas ..    
                for (String string : itens) {
                    if (string.startsWith(jaDigitado)) {
                        passou = true;
                        campo.setText(string);
                        campo.setCaretPosition(jaDigitado.length());
                        campo.setSelectionStart(jaDigitado.length());
                        campo.setSelectionEnd(string.length());
                        break;
                    }
                }
                if (!passou) {
                    jaDigitado = jaDigitado.substring(0, jaDigitado.length() - 1);//remover o ultimo
                }
            }
        };
        campo.addKeyListener(retorno);
        return retorno;
    }

    public static void dontChangeFocusLost(JFormattedTextField field) {
        field.setFocusLostBehavior(JFormattedTextField.PERSIST);
    }

    public static void msg(String menssagem) {
        JOptionPane.showMessageDialog(null, menssagem);
    }

    public static void addLineTable(javax.swing.JTable tabela, java.util.ArrayList linha) {
        DefaultTableModel modelo_tabela = (DefaultTableModel) tabela.getModel();
        modelo_tabela.addRow(linha.toArray());
        tabela.setModel(modelo_tabela);
    }

    public static void addLineTable(javax.swing.JTable tabela, String linha[]) {
        DefaultTableModel modelo_tabela = (DefaultTableModel) tabela.getModel();
        modelo_tabela.addRow(linha);
        tabela.setModel(modelo_tabela);
    }

    public static void addLineTable(javax.swing.JTable tabela, Object linha[]) {
        DefaultTableModel modelo_tabela = (DefaultTableModel) tabela.getModel();
        modelo_tabela.addRow(linha);
        tabela.setModel(modelo_tabela);
    }

    public static void addColumnTable(javax.swing.JTable tabela, String coluna) {
        DefaultTableModel modelo_tabela = (DefaultTableModel) tabela.getModel();
        modelo_tabela.addColumn(coluna);
        tabela.setModel(modelo_tabela);
    }

    public static DefaultTableModel getModelTable(javax.swing.JTable tabela) {
        return (DefaultTableModel) tabela.getModel();
    }

    public static void setModelTable(javax.swing.JTable tabela, javax.swing.table.DefaultTableModel modelo_tabela) {
        tabela.setModel(modelo_tabela);
    }

    /**
     * Pega o valor da coluna com a linha selecionada.
     *
     * @param table JTable
     * @param coluna Coluna que começa com 0
     * @return String com o valor
     */
    public static String getSelect(JTable table, int coluna) {
        if (table.getRowCount() < 1) {
            return "";
        }
        return table.getValueAt(table.getSelectedRow(), coluna).toString();
    }

    /**
     * Adiciona automaticamente a quantidade de 0 que for preciso.
     *
     * @param f O elemento a ser formatado.
     */
    public static void addZeros(final JFormattedTextField f) {
        f.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                return;
            }

            @Override
            public void focusLost(FocusEvent fe) {
                String zeros = "";
                MaskFormatter m = (MaskFormatter) f.getFormatter();
                int tamanho = m.getMask().length();
                String t = f.getText().trim();
                /*
                 * for (int i = 0; i < t.length(); i++) { if (t.charAt(i) ==
                 * "0".charAt(0)) { t = t.substring(i, t.length() - i); } else {
                 * break; } }
                 *
                 */
                for (int i = t.length(); i < tamanho; i++) {
                    zeros += "0";
                }
                f.setText(zeros + t);
            }
        });
    }
}
