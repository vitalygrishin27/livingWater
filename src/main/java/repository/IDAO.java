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

    User getJuryByUserName(String name);

    User getAdminByUserName(String name);

    List<Mark> getAllMarksFromDB();

    int getFreeIdOfMembersDB();

    int getFreeIdOfAddressDB();

    int getFreeIdOfSongDB();

    int getFreeTurnNumberFromMemberDB();

    int getFreeIdOfJuryDB();

    int getFreeIdOfMarkDB();

    boolean saveNewMemberIntoDB(Member member);

   Category getCategoryByName(String name);

   boolean isLoginForNewJuryCorrect(String login);

   boolean saveNewJuryIntoDB(User jury);

   Role createRoleByName(String name);

   boolean isMemberSoloByMemberId(int id);

    Member getMemberById(int id);

    boolean isMemberAlreadyEvaluated(String juryUserName, int memberId, int songNumber);

    List<Category> getAllCategoryFromDB();

    boolean saveMark(Member member, User jury, MARKCRITERIA markcriteria, Song song, int value);

    Song getSongById(int id);

    List<Mark> getListOfMarksBySong(Song song);

    List<Member> getMembersByCategory(Category category);

    List<Mark> getMarksByMember(Member member);

    List<MARKCRITERIA> getAllMarkCriteria();

  //  boolean saveNewEnsembleIntoDB(Member member);

    }





