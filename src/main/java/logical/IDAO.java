package logical;

import entity.User;

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

    List<User> getAllUsersFromDB();

    User getUserByName(String name);

}