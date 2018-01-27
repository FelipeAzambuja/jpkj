/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpkj;

import java.util.Scanner;

/**
 *
 * @author felipe
 */
public class Console {

    public static void write(Object texto) {
        System.out.print(texto);
    }

    public static void writeLine(Object texto) {
        System.out.println(texto);
    }    
    public static String readLine() {
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
