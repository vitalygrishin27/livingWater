import entity.Member;
import entity.Role;
import entity.User;
import logical.Repository;
import logical.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class Authentication {

    private static List<User> listOfJuriesOnline;
    private static List<User> admins;
    private static List<Role> roles;
    private static Map<String, User> sIds = new HashMap<>();
    private static Repository repository;
    private static List<Member> listOfMembers;
    private static Member currentMemberForEvaluation;


    static {
        System.out.println("Starting DB with MONGO");
        repository = Repository.getDAO("MONGO");
        listOfJuriesOnline = new ArrayList<>();
        //  admins= repository.getAllAdminsFromDB();
        roles = repository.getAllRolesFromDB();
        listOfMembers = repository.getAllMembersFromDB();
    }


    public static Member getCurrentMemberForEvaluation() {
        return currentMemberForEvaluation;
    }

    public static void setCurrentMemberForEvaluation(Member currentMemberForEvaluation) {
        Authentication.currentMemberForEvaluation = currentMemberForEvaluation;
    }

    public static void addToListOfJuriesOnline(User user) {
        listOfJuriesOnline.add(user);
    }

    public static boolean isJuryOnline(User user) {
        return listOfJuriesOnline.contains(user);
    }

    public static Repository getRepository() {
        return repository;
    }

    public static boolean isJuryOnlineByCoockiesSid(HttpServletRequest req) {
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
                }
            }
        } else {
            System.out.println(Utils.getCurrentTime() + " / No cookies present.");
            result = false;
        }
        return result;
    }


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

    public static List<User> getAllJuryFromDB() {
        return repository.getAllFromDBByRole(new Role(3, "JURY"));
    }

    public User getUserByUserNameFromDB(String userName) {
        return repository.getJuryByUserName(userName);

    }
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
