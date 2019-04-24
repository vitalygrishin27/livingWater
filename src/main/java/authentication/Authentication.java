package authentication;

import entity.Member;
import entity.Role;
import entity.Song;
import entity.User;
import repository.Repository;
import repository.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Authentication {

    private static List<User> listOfJuriesOnline;
    //   private static List<User> listOfJury;
    //   private static List<User> admins;
    //private static List<Role> roles;
    //   private static Map<String, User> sIds = new HashMap<>();
    private static Repository repository;
  //  private static List<Member> listOfMembers;
    private static Member currentMemberForEvaluation;
    private static Song currentSongForEvaluation;
    private static Map<String, Long> juryPingMap;
    private static Logger logger;

    static {

        //   System.out.println("Starting DB with MONGO");
        repository = Repository.getDAO("MONGO");
        //  System.out.println("Starting DB with SQL");
        //  repository = Repository.getDAO("SQL");
        //    listOfJury=repository.getAllFromDBByRole(new Role(3, "JURY"));

        listOfJuriesOnline = new ArrayList<>();
        //    roles = repository.getAllRolesFromDB();
        //  listOfMembers = repository.getAllMembersFromDB();
        juryPingMap = new HashMap<>();
        currentMemberForEvaluation = null;
        currentSongForEvaluation = null;


        logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {

            // This block configure the logger with handler and formatter
           String fileName="livingWater." + System.currentTimeMillis() + ".log";
            fh = new FileHandler(fileName);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            // fh.setFormatter(formatter);

            fh.setFormatter(new java.util.logging.Formatter() {
                @Override
                public String format(LogRecord record) {
                    SimpleDateFormat logTime = new SimpleDateFormat("dd--MM--yyyy HH:mm:ss");
                    Calendar cal = new GregorianCalendar();
                    cal.setTimeInMillis(record.getMillis());
                    return record.getLevel()
                            + " " + logTime.format(cal.getTime())
                       //     + " || "
                      //      + record.getSourceClassName().substring(
                        //    record.getSourceClassName().lastIndexOf(".") + 1,
                     //       record.getSourceClassName().length())
                        //    + "."
                      //      + record.getSourceMethodName()
                           + "   "
                            + record.getMessage() + "\n";
                }
            });


         //   String command = "powershell.exe d:\\1.txt";
            // Executing the command
        //    Process powerShellProcess = Runtime.getRuntime().exec(command);
            // Getting the results
         //   powerShellProcess.getOutputStream().close();
        //    PowerShellResponse response = PowerShell.executeSingleCommand("Get-Content d:\\1.txt -Wait");
       //     System.out.println("List Processes:" + response.getCommandOutput());


            // the following statement is used to log any messages
     //       Runtime.getRuntime().exec("cmd.exe");
       //     Runtime.getRuntime().exec("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe");
     //       Runtime.getRuntime().exec("calc.exe");
          //  Runtime.getRuntime().exec("powerShell.exe Get-Content "+fileName+" -Wait -Tail 0");
            logger.info("STARTING SERVER");

            // Чтобы удалить обработчик консоли, используйте
         //   logger.setUseParentHandlers(false);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }


    }

    public static void log(String message) {
        logger.info(message);
    }

    public static void ping(String juryUserName) {
        Date date = new Date();
        System.out.println(date.getTime());
        juryPingMap.put(juryUserName, date.getTime());
    }

    public static Long getSecondsAfterPingJury(String juryUserName) {
        Date date = new Date();

        long timeElapseAfterPing;
        //   new TimeUnit().convert()

        if (juryPingMap.containsKey(juryUserName)) {
            timeElapseAfterPing = date.getTime() - juryPingMap.get(juryUserName);
        } else {
            timeElapseAfterPing = 0;
        }

        return timeElapseAfterPing;
    }


    public static Song getCurrentSongForEvaluation() {
        return currentSongForEvaluation;
    }

    public static Member getCurrentMemberForEvaluation() {

        return currentMemberForEvaluation;
    }

    public static void setCurrentMemberForEvaluation(Member currentMemberForEvaluation, int songNumber) {
        System.out.println(Utils.getCurrentTime() + " / Set to current Member successful.");

        Authentication.currentMemberForEvaluation = currentMemberForEvaluation;
        if (songNumber == 1) Authentication.currentSongForEvaluation = currentMemberForEvaluation.getFirstSong();
        if (songNumber == 2) Authentication.currentSongForEvaluation = currentMemberForEvaluation.getSecondSong();
    }

    public static void addToListOfJuriesOnline(User user) {
        listOfJuriesOnline.add(user);
    }

    public static boolean isJuryAlreadyInListOnline(User user) {
        return listOfJuriesOnline.contains(user);
    }

    public static Repository getRepository() {
        return repository;
    }

    public static boolean isJuryInDbByCookies(HttpServletRequest req) {
        boolean result = false;
        if (req.getCookies() != null) {

            for (Cookie co : req.getCookies()
            ) {
                String userName = co.getValue();
                User user = repository.getJuryByUserName(userName);
                if (user != null) {
                    if (listOfJuriesOnline.contains(user)) {
                        result = true;
                        break;
                    }
                    listOfJuriesOnline.add(user);
                    result = true;
                    break;
                }
            }
        } else {
            System.out.println(Utils.getCurrentTime() + " / No cookies present.");
            result = false;
        }
        return result;
    }


    public static boolean isAdminInDbByCookies(HttpServletRequest req) {
        boolean result = false;
        if (req.getCookies() != null) {

            for (Cookie co : req.getCookies()
            ) {
                String userName = co.getValue();
                User user = repository.getAdminByUserName(userName);
                if (user != null) {

                    result = true;
                    break;

                }
            }
        } else {
            System.out.println(Utils.getCurrentTime() + " / No cookies present.");
            result = false;
        }
        return result;
    }

    public static List<User> getAllJury() {
        return repository.getAllFromDBByRole(new Role(3, "JURY"));
    }
/*
    public static boolean isAuthenticated(HttpServletRequest req, String role) {

        String sId = "";
        if (req.getCookies() != null) {
            for (Cookie co : req.getCookies()
            ) {
                sId = co.getValue();
            }
        } else {
            System.out.println("No cookies present.");
            sId = "00000";
        }


        for (Map.Entry element : sIds.entrySet()
        ) {
            if (element.getKey().equals(sId)) {
                System.out.print("SID= " + sId + " is presented in localhost map ");
                if (sIds.get(sId).getRole().equals(role)) {
                    System.out.println("with role: " + role + ". STATUS: OK.");
                    return true;
                }
                System.out.println("but role: " + sIds.get(sId)
                        .getRole() + " is not correct to access the page. STATUS: ERROR.");
                return false;

            }
        }
        System.out.println("Authorization crashed. sId with '" + sId + "' is absent. Will redirect to LOG Page.");
        return false;
    }


    public static boolean removeSId(String sId) {
        sIds.remove(sId);
        return true;
    }

/*
    public static String getSIdAuthenticate(User user) {
        if (repository.isUserAlreadyRegistered(user)) {
            if (repository.isPasswordRight(user)) {

                String newSId = user.getUserName() + "_" +
                        ((new Random().nextInt(20) + 1) *
                                (new Random().nextInt(20) + 1));
                sIds.put(newSId, user);
                return newSId;
            }
        }

        return "ERROR";
    }
*/

    //   public static List<User> getAllJuryFromDB() {
    //       return repository.getAllFromDBByRole(new Role(3, "JURY"));
    //   }

    //   public static User getJuryByUserNameFromDB(String userName) {
    //       return repository.getJuryByUserName(userName);

    //   }

    //  public static String getUserNameFromCookies(HttpServletRequest req){


    //  }


    //  public static String getRoleBySId(String sId) {
//
    //       return sIds.get(sId).getRole();
//    }

 /*   public static void addSIdToMap(HttpServletRequest req){
        String sId = "";
        if (req.getCookies() != null) {
            for (Cookie co : req.getCookies()
            ) {
                sId = co.getValue();
            }
        } else {
            System.out.println("No cookies present.");
            sId = "00000";
        }
        if(!sIds.containsKey(sId)){
            int index=sId.indexOf('_');
            String name=sId.substring(0,index);
            System.out.println("Method addSidsToMap done correctly. userName="+name);
            users.add(repository.getUserByName(name));
            sIds.put(sId,repository.getUserByName(name));
        }else{
            System.out.println("sId '"+sId+"' is already presented in sIds.");
            System.out.println("Error with method.");
        }
    }*/


/*
   public static List<User> getJuryList(){
        List<User> result=new ArrayList<>();
        for (User element:users
             ) {
            if(element.getRole().equals("USER")){
                result.add(element);
            }
        }
        return result;
    }
*/


}
