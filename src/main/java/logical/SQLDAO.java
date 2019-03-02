package logical;

import entity.Role;
import entity.User;

import java.util.List;

public class SQLDAO extends Repository {
    private static boolean single=false;
    private static SQLDAO sqldao;
    public static Repository getDAO() {
        if (single) {
            return sqldao;
        }
        single=true;
        return new SQLDAO();
    }

    private SQLDAO(){
//Конструктор
    }

    @Override
    public boolean isUserAlreadyRegistered(User user) {
        return false;
    }

    @Override
    public boolean isPasswordRight(User user) {
        return false;
    }

    @Override
    public String getROLE(User user) {
        return null;
    }

    @Override
    public List<Role> getAllRolesFromDB() {
        return null;
    }

    @Override
    public List<User> getAllFromDBByRole(Role role) {
        return null;
    }

    @Override
    public User getUserByUserName(String name) {
        return null;
    }
}
