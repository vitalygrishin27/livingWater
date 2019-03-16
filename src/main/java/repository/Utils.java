package repository;

import authentication.Authentication;
import entity.*;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {

    public static JSONObject getJsonFromRequest(HttpServletRequest req) {
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

    public static Member getSoloMemberFromJson(JSONObject jsonObject) {
        return BuilderMember.getBuilderMember().setId(Authentication.getRepository().getFreeIdOfMembersDB())
                .setFirstName(jsonObject.getString("firstname"))
                .setSecondName(jsonObject.getString("secondname"))
                .setLastName(jsonObject.getString("lastname"))
                .setBirth(getDateFromString(jsonObject.getString("birth")))
                .setEnsembleName("")
                .setCountOfMembers(1)
                .setGender(Gender.getGenderByChar(jsonObject.getString("gender")))
                .setAddress(getAddressFromJson(jsonObject))
                .setPassport(jsonObject.getString("passport"))
                .setINN(jsonObject.getString("INN"))
                .setBoss(jsonObject.getString("boss"))
                .setCategory(Authentication.getRepository().getCategoryByName(jsonObject.getString("category")))
                .setFirstSong(createFirstSongByName(jsonObject.getString("firstSong")))
                .setSecondSong(createSecondSongByName(jsonObject.getString("secondSong")))
                .setRegistration(false)
                .setTurnNumber(Authentication.getRepository().getFreeTurnNumberFromMemberDB())
                .build();

    }


    public static User getJuryFromJson(JSONObject jsonObject) {
// TODO: 16.03.2019
        return null;
    }

    private static Song createFirstSongByName(String name) {
        Song song = new Song();
        song.setId(Authentication.getRepository().getFreeIdOfSongDB());
        song.setName(name);
        return song;
    }

    private static Song createSecondSongByName(String name) {
        Song song = new Song();
        song.setId((Authentication.getRepository().getFreeIdOfSongDB()) + 1);
        song.setName(name);
        return song;
    }


    private static Address getAddressFromJson(JSONObject jsonObject) {
        return BuilderAddress.getBuilderAddress().setId(Authentication.getRepository().getFreeIdOfAddressDB())
                .setCountry(jsonObject.getString("country"))
                .setRegion(jsonObject.getString("region"))
                .setDistrict(jsonObject.getString("district"))
                .setCity(jsonObject.getString("city"))
                .setPhone(jsonObject.getString("phone"))
                .build();


    }


    private static Date getDateFromString(String d) {
        Date docDate = null;
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");
        try {
            docDate = format.parse(d);
        } catch (ParseException e) {
            System.out.println("Ошибка в приведении даты из строки (" + d + ")");
            e.printStackTrace();
        }
        return docDate;
    }


    public static String getCurrentTime() {
        GregorianCalendar gcalendar = new GregorianCalendar();
        return gcalendar.get(Calendar.HOUR) + ":" +
                gcalendar.get(Calendar.MINUTE) + ":" +
                gcalendar.get(Calendar.SECOND);

    }

}
