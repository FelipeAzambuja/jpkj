package jpkj;

import java.util.regex.Pattern;
import org.joda.time.DateTime;

public class Calendar {

    DateTime calendar;

    public DateTime getJoda() {
        return this.calendar;
    }

    public Calendar() {
        this.calendar = new DateTime();
    }

    public Calendar(String data) {
        this.calendar = new DateTime();
        this.parse(data);
    }

    public void parse(String data) {
        data = data.trim();
        if (data.isEmpty()) {
            return;
        }
        String separadorDataHora = " ";
        boolean temhora = false;
        boolean temdata = false;
        temdata = Pattern.matches("([0-9]{2,4})(-|/|\\.)([0-9]{2})(-|/|\\.)([0-9]{2,4})", data.split(separadorDataHora)[0]);
        if(data.split(separadorDataHora).length > 1){
            temhora = Pattern.matches(".{9,12}([0-9]{2}):([0-9]{2})(:([0-9]{2})){0,1}", data.split(separadorDataHora)[1]);
        }
        if (temhora) {
            separadorDataHora = data.substring(10, 11);
        }
        String[] split = data.split(Pattern.quote(separadorDataHora));
        String[] split1 = split[0].split("(-|/|\\.)");
        int ano = 0, mes = 0, dia = 0;
        if (temdata) {
            if (split1[0].length() == 4) {
                ano = Integer.parseInt(split1[0]);
                mes = Integer.parseInt(split1[1]);
                dia = Integer.parseInt(split1[2]);
            } else {
                ano = Integer.parseInt(split1[2]);
                mes = Integer.parseInt(split1[1]);
                dia = Integer.parseInt(split1[0]);
            }
        } else {
            ano = calendar.getYear();
            mes = calendar.getMonthOfYear();
            dia = calendar.getDayOfMonth();
        }
        int hora = 0, minuto = 0, segundo = 0;
        if (temhora) {
            String[] split2 = split[1].split(":");
            hora = Integer.parseInt(split2[0]);
            minuto = Integer.parseInt(split2[1]);
            if (split2.length == 3) {
                segundo = Integer.parseInt(split2[3]);
            } else {
                segundo = 0;
            }
        } else {
            hora = calendar.getHourOfDay();
            minuto = calendar.getMinuteOfHour();
            segundo = calendar.getSecondOfMinute();
        }
        this.calendar = new DateTime(ano, mes, dia, hora, minuto, segundo);
    }

    public void modify(String value) {
        String[] fofinho = value.split(Pattern.quote(" "));
        for (int i = 0; i < fofinho.length; i++) {
            String type = fofinho[i];
            i++;
            int v = Integer.parseInt(fofinho[i]);
            i++;
            String mod = fofinho[i];
            if (type.equalsIgnoreCase("+")) {
                if (mod.equalsIgnoreCase("DAYS")) {
                    calendar = calendar.plusDays(v);
                } else if (mod.equalsIgnoreCase("MONTHS")) {
                    calendar = calendar.plusMonths(v);
                } else if (mod.equalsIgnoreCase("YEARS")) {
                    calendar = calendar.plusYears(v);
                } else if (mod.equalsIgnoreCase("HOURS")) {
                    calendar = calendar.plusHours(v);
                } else if (mod.equalsIgnoreCase("MINUTES")) {
                    calendar = calendar.plusMinutes(v);
                } else if (mod.equalsIgnoreCase("SECONDS")) {
                    calendar = calendar.plusSeconds(v);
                }
            } else if (type.equalsIgnoreCase("-")) {
                if (mod.equalsIgnoreCase("DAYS")) {
                    calendar = calendar.minusDays(v);
                } else if (mod.equalsIgnoreCase("MONTHS")) {
                    calendar = calendar.minusMonths(v);
                } else if (mod.equalsIgnoreCase("YEARS")) {
                    calendar = calendar.minusYears(v);
                } else if (mod.equalsIgnoreCase("HOURS")) {
                    calendar = calendar.minusHours(v);
                } else if (mod.equalsIgnoreCase("MINUTES")) {
                    calendar = calendar.minusMinutes(v);
                } else if (mod.equalsIgnoreCase("SECONDS")) {
                    calendar = calendar.minusSeconds(v);
                }
            }

        }
    }

    /**
     *
     * @param format yyyy = year MM = month dd =day HH = hour mm = minute ss =
     * seconds SSS = microsegunds ZZ = timezone
     * @return formated string
     */
    public String format(String format) {
        String retorno = "";
        retorno = this.calendar.toString(format);
        return retorno;
    }
    
    public String format(String format,String mod) {
        this.modify(mod);
        String retorno = "";
        retorno = this.calendar.toString(format);
        return retorno;
    }
}
