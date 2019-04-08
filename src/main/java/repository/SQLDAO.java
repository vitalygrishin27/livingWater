package repository;

import entity.*;
import hibernateUtils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class SQLDAO extends Repository {
    private static boolean single = false;
    private static SQLDAO sqldao;

    public static Repository getDAO() {
        if (single) {
            return sqldao;
        }
        single = true;
        return new SQLDAO();
    }

    private static SessionFactory sessionFactory;
    private static Session session = null;

    private static void getSessionForDb(){
        SessionFactory getSession = null;
        HibernateSessionFactoryUtil.getInstance();

        try {
            getSession = HibernateSessionFactoryUtil.getSession();
            session = getSession.openSession();
            Transaction transaction = session.beginTransaction();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    private SQLDAO() {
        getSessionForDb();
    }

    @Override
    public boolean isUserAlreadyRegistered(User user) {
        //проверить есть ли юзер в базе
        @SuppressWarnings("JpaQlInspection")
        Query queryUserIsRegistered = session.createQuery("select userName FROM users where userName =  :paramName");
        queryUserIsRegistered.setParameter("paramName",user.getUserName());
        //return queryUserIsRegistered.list().isEmpty();
        return true;
    }


    @Override
    public boolean isPasswordRight(User user) {
        //соответствует ли пассворд юзеру в базе
/*
        Query queryUserPass = session.createQuery("SELECT  password from users  where username = :paramName and  password = :paramPassword");
        queryUserPass.setParameter("paramName",user.getUserName());
        queryUserPass.setParameter("paramPassword",user.getPassword());
        return !queryUserPass.list().isEmpty();*/
         return true;
    }

    @Override
    public String getROLE(User user) {
       /*
        Query queryUserPass = session.createQuery("select role from entity.User role where role = :paramName");
        queryUserPass.setParameter("paramName",user.getUserName());*/
        return user.getRole().toString();
    }

    @Override
    public List<Role> getAllRolesFromDB() {
        //получить список всех ролей
        List <Role> roleList = new ArrayList<>();
        Query queryGetAllRoles = session.createQuery("select r from entity.Role r  ");
        int listSize = queryGetAllRoles.list().size();
        for (int i = 0; i < listSize ; i++) {
            roleList.add((Role) queryGetAllRoles.list().get(i));
        }

        return roleList;
    }

    @Override
    public List<User> getAllFromDBByRole(Role role) {
        //получить всех юзеров по роли

        return null;
    }

    @Override
    public User getJuryByUserName(String name) {
        //создаем юзера(жюри) по имени username(первичный ключ)
        return null;
    }

    @Override
    public List<Member> getAllMembersFromDB() {
        //получаем всех участников
        return null;
    }

    @Override
    public User getAdminByUserName(String name) {
        //создаем юзера(админа) по имени username(первичный ключ)
        return null;
    }


    @Override
    public List<Mark> getAllMarksFromDB() {
        //получаем все оценки(все данные: кто поставил....) и создаем список
        return null;
    }

    @Override
    public int getFreeIdOfMembersDB() {
        //для создания Id Member
        //сейчас стоит у меня автогенерация. Отключить. И самой получать последний Id и инкрементить его. Мне знать Id заранее обязательно!
        return 0;
    }

    @Override
    public int getFreeIdOfAddressDB() {
        //аналогино с мемберсами
        return 0;
    }

    @Override
    public int getFreeIdOfSongDB() {
        return 0;
    }

    @Override
    public int getFreeTurnNumberFromMemberDB() {
        //аналогично с получением ИД у Мемберсов,но поле TurnNumber
        return 0;
    }

    @Override
    public boolean saveNewMemberIntoDB(Member member) {
        //сохраняем Мембера в БД
        return false;
    }

    @Override
    public Category getCategoryByName(String name) {
        return null;
    }

    @Override
    public boolean isLoginForNewJuryCorrect(String login) {
        //когда регестрируем нового жюри,проверяем перед добавлением уникальный ли логин,так как это первичный ключ
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
        //если солист,то тру
        //если название ансамбля пустое,то он солист
        return false;
    }


    @Override
    public Member getMemberById(int id) {
        return null;
    }

    @Override
    public boolean isMemberAlreadyEvaluated(String juryUserName, int memberId, int songNumber) {
        //смотрим в таблицу Оценок(марк)
        //берем все оценки, отбираем по жюри, мемберу и номеру песни. Если такая запись есть,то тру
        return false;
    }

    @Override
    public List<Category> getAllCategoryFromDB() {
        return null;
    }

    @Override
    public boolean saveMark(Member member, User jury, MARKCRITERIA markcriteria, Song song, int value) {
        // сохраняем оценку в таблицу Марк,если все ок то ТРУ
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

    @Override
    public List<Mark> getListOfMarksBySong(Song song) {
        return null;
    }

    @Override
    public List<Member> getMembersByCategory(Category category) {
        return null;
    }

    @Override
    public List<Mark> getMarksByMember(Member member) {
        return null;
    }

    @Override
    public List<MARKCRITERIA> getAllMarkCriteria() {
        // ХЗ что хотел автор
        return null;
    }


    class А implements Runnable{

        @Override
        public void run() {

        }
    }
}
