package jpkj;

public class Log {

    public static String cout = "cmd";
    public static String in() {
        return Console.readLine();
    }

    public static void out(Object text) {
        if (Log.cout.equals("cmd")) {
            Console.writeLine(text);
        } else {
            if (UtilFile.exists(Log.cout)) {
                text = UtilFile.load(Log.cout) + "\n" + text;
            }
            text += "|"+new Calendar().format(null);
            UtilFile.write(Log.cout, text.toString());
        }
    }
}
