package repository;

import entity.*;

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
    public User getJuryByUserName(String name) {
        return null;
    }

    @Override
    public List<Member> getAllMembersFromDB() {
        return null;
    }

    @Override
    public User getAdminByUserName(String name) {
        return null;
    }


    @Override
    public List<Mark> getAllMarksFromDB() {
        return null;
    }

    @Override
    public int getFreeIdOfMembersDB() {
        return 0;
    }

    @Override
    public int getFreeIdOfAddressDB() {
        return 0;
    }

    @Override
    public int getFreeIdOfSongDB() {
        return 0;
    }

    @Override
    public int getFreeTurnNumberFromMemberDB() {
        return 0;
    }

    @Override
    public boolean saveNewMemberIntoDB(Member member) {
        return false;
    }

    @Override
    public Category getCategoryByName(String name) {
        return null;
    }

    @Override
    public boolean isLoginForNewJuryCorrect(String login) {
        return false;
    }

    @Override
    public boolean saveNewJuryIntoDB(User jury) {
        return false;
    }

    @Override
    public int getFreeIdOfJuryDB() {
        return 0;
    }

    @Override
    public Role createRoleByName(String name) {
        return null;
    }

    @Override
    public boolean isMemberSoloByMemberId(int id) {
        return false;
    }


    @Override
    public Member getMemberById(int id) {
        return null;
    }

    @Override
    public boolean isMemberAlreadyEvaluated(String juryUserName, int memberId, int songNumber) {
        return false;
    }

    @Override
    public List<Category> getAllCategoryFromDB() {
        return null;
    }

    @Override
    public boolean saveMark(Member member, User jury, MARKCRITERIA markcriteria, Song song, int value) {
        return false;
    }

    @Override
    public Song getSongById(int id) {
        return null;
    }

    @Override
    public int getFreeIdOfMarkDB() {
        return 0;
    }
}
