package repository;

import entity.*;

import java.util.List;

public interface IDAO {
    //Repository должен иметь статический метод и поле для реализации singleton
    //   В конструкторе Repository установить значение true переменной single
    // private static boolean single;
    /*public static Repository getDAO() {
        if (single) {
            return DAO;
        }
        return new DAO();
    } */

    //  boolean isUserAlreadyRegistered(String userName);
    boolean isUserAlreadyRegistered(User user);

    //   boolean isPasswordRight(String userName, String pass);
    boolean isPasswordRight(User user);

    //   boolean registerUser(String userName, String password);
    //   boolean registerUser(User user);
    String getROLE(User user);

    List<Role> getAllRolesFromDB();

    List<User> getAllFromDBByRole(Role role);

    List<Member> getAllMembersFromDB();

    Address getAddressById(int id);

    Song getSongById(int id);

    User getJuryByUserName(String name);

    User getAdminByUserName(String name);

}