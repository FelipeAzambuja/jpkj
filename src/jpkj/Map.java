/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpkj;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 *
 * @author Administrator
 * @param <K>
 * @param <V>
 */
public class Map<K extends Object, V extends Object> extends HashMap<K, V> {

    public List<Object> getKeys() {
        List<Object> r = new List<Object>();
        for (Object object : this.keySet()) {
            r.add(object);
        }
        return r;
    }

    public String toString(Object key) {
        if (this.get(key) == null) {
            return "";
        } else {
            return this.get(key).toString();
        }
    }

    public Integer toInteger(Object key) {
        return Integer.parseInt(this.toString(key));
    }

    public Float toFloat(Object key) {
        return Float.parseFloat(this.toString(key));
    }

    public Double toDouble(Object key) {
        return Double.parseDouble(this.toString(key));
    }

    public String toCurrency(Object key, Locale local) {
        String retorno = "";
        NumberFormat.getCurrencyInstance(local);
        retorno = NumberFormat.getCurrencyInstance().format(this.toString(key));
        return retorno;
    }

    public String toCurrency(Object key) {
        return this.toCurrency(key, Locale.getDefault());
    }

    public Calendar toCalendar(Object key) {
        return (new Calendar(this.toString(key)));
    }

}
