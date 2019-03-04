package logical;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Utils {

    public static JSONObject getJsonFromRequest(HttpServletRequest req){
        JSONObject result;

        StringBuilder jb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);


            result = new JSONObject(jb.toString());
            System.out.println(result);

            return result;
        } catch (Exception e) {
            System.out.println("Error with buffered reader in getJsonFromRequest method.");
        }

        return new JSONObject();
    }

    public static String getCurrentTime(){
        GregorianCalendar gcalendar = new GregorianCalendar();
        return  gcalendar.get(Calendar.HOUR) + ":"+
                gcalendar.get(Calendar.MINUTE) + ":"+
                gcalendar.get(Calendar.SECOND);

    }

}
