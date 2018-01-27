/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpkj;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author Administrator
 * @param <E>
 */
public class List<E extends Object> extends ArrayList<E> {

    public String[] toStringArray() {
        String[] r = new String[this.size()];
        for (int i = 0; i < this.size(); i++) {
            r[i] = this.get(i).toString();
        }
        return r;
    }

    public String toString(int key) {
        return this.get(key).toString();
    }

    public Integer[] toIntegerArray() {
        Integer[] r = new Integer[this.size()];
        for (int i = 0; i < this.size(); i++) {
            r[i] = Integer.parseInt(this.get(i).toString());
        }
        return r;
    }

    public Integer toInteger(int key) {
        return Integer.parseInt(this.toString(key));
    }

    public Float[] toFloatArray() {
        Float[] r = new Float[this.size()];
        for (int i = 0; i < this.size(); i++) {
            r[i] = Float.parseFloat(this.get(i).toString());
        }
        return r;
    }

    public Float toFloat(int key) {
        return Float.parseFloat(this.toString(key));
    }

    public Double[] toDoubleArray() {
        Double[] r = new Double[this.size()];
        for (int i = 0; i < this.size(); i++) {
            r[i] = Double.parseDouble(this.get(i).toString());
        }
        return r;
    }

    public Double toDouble(int key) {
        return Double.parseDouble(this.toString(key));
    }

    public String toCurrency(int key, Locale local) {
        String retorno = "";
        NumberFormat.getCurrencyInstance(local);
        retorno = NumberFormat.getCurrencyInstance().format(this.toString(key));
        return retorno;
    }

    public String toCurrency(int key) {
        return this.toCurrency(key, Locale.getDefault());
    }

    public Calendar[] toCalendarArray() {
        Calendar[] r = new Calendar[this.size()];
        for (int i = 0; i < this.size(); i++) {
            r[i] = new Calendar(this.get(i).toString());
        }
        return r;
    }

    public Calendar toCalendar(int key) {
        return (new Calendar(this.toString(key)));
    }

    @Override
    public E get(int i) {
        if (i > this.size()) {
            return null;
        } else {
            return super.get(i);
        }
    }

}
