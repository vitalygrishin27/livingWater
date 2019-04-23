package repository;

import entity.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import hibernateUtils.HibernateSessionFactoryUtil;
import org.hibernate.TransactionException;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class SQLDAO extends Repository {
    private static boolean single;
    private static SQLDAO sqldao;

    public static Repository getDAO() {
        if (single) {
            return sqldao;
        }
        return new SQLDAO();
    }

    private static SessionFactory sessionFactory;
    private static Session session;

    private SQLDAO() {
        single = true;
        getSessionDb();
    }

    private void getSessionDb() {
        try {
            sessionFactory = HibernateSessionFactoryUtil.getSession();
            session = sessionFactory.openSession();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    @Override
    public boolean isUserAlreadyRegistered(User user) {
        //проверить есть ли юзер в базе
        session.beginTransaction();
        @SuppressWarnings("JpaQlInspection")
        Query queryUserByName = session.createQuery(" from User s where s.userName = :paramName");
        queryUserByName.setParameter("paramName", user.getUserName());
        List<User> users = queryUserByName.getResultList();
        session.getTransaction().commit();

        if (users.isEmpty())
            return false;
        else
            return true;


    }

    @Override
    public boolean isPasswordRight(User user) {
        //соответствует ли пассворд юзеру в базе
        session.beginTransaction();
        @SuppressWarnings("JpaQlInspection")
        Query checkedPassword = session.createQuery(" from User s where s.password = :paramPass and s.userName = :paramName");
        checkedPassword.setParameter("paramPass", user.getPassword());
        checkedPassword.setParameter("paramName", user.getUserName());
        List<User> users = checkedPassword.getResultList();
        session.getTransaction().commit();

        if (users.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public String getROLE(User user) {
        //получить роль
        session.beginTransaction();
        @SuppressWarnings("JpaQlInspection")
        Query getRoleQuery = session.createQuery("select r.name from Role r, User u where r.id = u.role and u.username = :paramName");
        getRoleQuery.setParameter("paramName", user.getUserName());
        List<Role> roles = getRoleQuery.list();
        session.getTransaction().commit();
        if (!roles.isEmpty())
            return roles.get(0).getName();
        else
            return "UNKNOWN";
    }

    @Override
    public List<Role> getAllRolesFromDB() {
        session.beginTransaction();
        @SuppressWarnings("JpaQlInspection")
        Query allRoles = session.createQuery(" from Role r ");
        List<Role> roles = allRoles.getResultList();
        session.getTransaction().commit();
        return roles;

    }

    @Override
    public List<User> getAllFromDBByRole(Role role) {
        //получить всех юзеров по роли
        List<User> users = null;
        try {
            session.beginTransaction();
            @SuppressWarnings("JpaQlInspection")
            Query getUsersByRole = session.createQuery("select u.userName,u.password,u.firstName" +
                    ",u.secondName,u.lastName,u.office from User u  where u.role = :paramRole");
            getUsersByRole.setParameter("paramRole", role.getId());
            users = getUsersByRole.getResultList();
            session.getTransaction().commit();
        } catch (TransactionException ex) {
            ex.getStackTrace();
        } finally {
            session.close();
        }


        return users;
    }

    @Override
    public User getJuryByUserName(String name) {

        return getUser(name);

    }

    private User getUser(String name) {
        List<Object[]> users = null;

        try {
            session.beginTransaction();
            @SuppressWarnings("JpaQlInspection")
            Query queryUserByName = session.createQuery(" select u.userName,u.firstName" +
                    ",u.secondName,u.lastName,u.office,u.password from User u where u.userName = :paramName");
            queryUserByName.setParameter("paramName", name);
            users = queryUserByName.list();
            for (Object[] result : users) {
                return BuilderUserJury
                        .getBuilderUserJury()
                        .setUserName((String) result[0])
                        .setFirstName((String) result[1])
                        .setSecondName((String) result[2])
                        .setLastName((String) result[3])
                        .setOffice((String) result[4])
                        .setPassword((String) result[5]).build();
            }
            session.getTransaction().commit();
        } catch (TransactionException ex) {
            ex.getStackTrace();
        }

        return null;
    }

    @Override
    public List<Member> getAllMembersFromDB() {
        //получаем всех участников
        session.beginTransaction();
        //noinspection JpaQlInspection
        List<Member> members = session.createQuery("From Member").list();
        session.getTransaction().commit();


        return members;

    }

    @Override
    public User getAdminByUserName(String name) {
        return getUser(name);
    }


    @Override
    public List<Mark> getAllMarksFromDB() {
        //получаем все оценки(все данные: кто поставил....) и создаем список
        session.beginTransaction();
        //noinspection JpaQlInspection
        List<Mark> marks = session.createQuery("From Mark").list();
        session.getTransaction().commit();
        return marks;
    }

    @Override
    public synchronized int getFreeIdOfMembersDB() {
        //для создания Id Member
        //сейчас стоит у меня автогенерация. Отключить. И самой получать последний Id и инкрементить его. Мне знать Id заранее обязательно!
        session.beginTransaction();
        //noinspection JpaQlInspection
        List<Member> members = session.createQuery("select m.id From Member m").list();
        session.getTransaction().commit();
        int min = Integer.MAX_VALUE;
        if (members.size() == 0) {
            return 0;
        }
        for (int i = 0; i < members.size(); i++) {
            int cur = members.get(i).getId();
            if (cur < min) {
                min = cur;
            }

        }
        return min;
        //return getId(members);
    }

    @Override
    public synchronized int getFreeIdOfAddressDB() {
        //аналогино с мемберсами
        session.beginTransaction();
        //noinspection JpaQlInspection
        List<Address> addresses = session.createQuery("select  a.id From Address a").list();
        session.getTransaction().commit();
        int min = Integer.MAX_VALUE;
        if (addresses.size() == 0) {
            return 0;
        }
        for (int i = 0; i < addresses.size(); i++) {
            int cur = addresses.get(i).getId();
            if (cur < min) {
                min = cur;
            }

        }
        return min;
        // return getId(addresses);
    }

    @Override
    public synchronized int getFreeIdOfSongDB() {

        session.beginTransaction();
        //noinspection JpaQlInspection
        List<Song> songs = session.createQuery("select  s.id From Song s").list();
        session.getTransaction().commit();
        int min = Integer.MAX_VALUE;
        if (songs.size() == 0) {
            return 0;
        }
        for (int i = 0; i < songs.size(); i++) {
            int cur = songs.get(i).getId();
            if (cur < min) {
                min = cur;
            }

        }
        return min;
        //return getId(songs);
    }


    @Override
    public synchronized int getFreeTurnNumberFromMemberDB() {
        //аналогично с получением ИД у Мемберсов,но поле TurnNumber
        session.beginTransaction();
        //noinspection JpaQlInspection
        List<Member> members = session.createQuery("select m.turnNumber From Member m").list();
        session.getTransaction().commit();
        int min = Integer.MAX_VALUE;
        if (members.size() == 0) {
            return 0;
        }
        for (Member mmb : members) {
            int cur = mmb.getTurnNumber();
            if (cur < min) {
                min = cur;
            }

        }
        return min;

    }

    @Override
    public synchronized boolean saveNewMemberIntoDB(Member member) {
        //сохраняем Мембера в БД
        try {
            session.beginTransaction();
            session.save(member);
            session.getTransaction().commit();
        } catch (HibernateException ex) {

            ex.getStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Category getCategoryByName(String name) {
        List<Object[]> categories = null;
        try {
            session.beginTransaction();
            //noinspection JpaQlInspection
            Query queryCategories = session.createQuery("select c.id, c.name From Category c where r.name = :paramName");
            queryCategories.setParameter("paramName", name);
            categories = queryCategories.list();
            session.getTransaction().commit();
            for (Object[] c : categories) {
                return new Category((Integer) c[0], (String) c[1]);
            }
        } catch (TransactionException e) {
            e.getStackTrace();
        }
        return null;

    }

    @Override
    public boolean isLoginForNewJuryCorrect(String login) {
        List users = null;
        //когда регестрируем нового жюри,проверяем перед добавлением уникальный ли логин,так как это первичный ключ
        try {
            session.beginTransaction();
            @SuppressWarnings("JpaQlInspection")
            Query queryUserByName = session.createQuery(" select u.userName from User u where u.userName = :paramName");
            queryUserByName.setParameter("paramName", login);
            users = queryUserByName.list();
            session.getTransaction().commit();
            if (users.isEmpty())
                return true;
            else
                return false;

        } catch (TransactionException ex) {
            ex.getStackTrace();
        }
        return false;

    }

    @Override
    public synchronized boolean saveNewJuryIntoDB(User jury) {
        try {
            session.beginTransaction();
            session.save(jury);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException ex) {
            ex.getStackTrace();
            return false;
        }

    }

    @Override
    public synchronized int getFreeIdOfJuryDB() {
        session.beginTransaction();
        //noinspection JpaQlInspection
        List<User> jury = session.createQuery("select  u.id From User u").list();
        session.getTransaction().commit();
        int min = Integer.MAX_VALUE;
        if (jury.size() == 0) {
            return 0;
        }
        for (int i = 0; i < jury.size(); i++) {
            int cur = jury.get(i).getId();
            if (cur < min) {
                min = cur;
            }

        }
        return min;
    }

    @Override
    public Role createRoleByName(String name) {
        List<Object[]> roles = null;
        //noinspection JpaQlInspection
        roles = session.createQuery("select r.id, r.name From Role r where r.name = :paramName").list();
        for (Object[] r : roles) {
            return new Role((Integer) r[0], (String) r[1]);
        }
        return null;
    }

    @Override
    public boolean isMemberSoloByMemberId(int id) {
        //если солист,то тру
        //если название ансамбля пустое,то он солист
        List<Object> members = null;
        String result = null;
        try {
            session.beginTransaction();
            @SuppressWarnings("JpaQlInspection")
            Query queryMembers = session.createQuery(" select m.ensembleName from Member m where m.id = :id");
            queryMembers.setParameter("id", id);
            members = queryMembers.list();
            session.getTransaction().commit();
        } catch (TransactionException ex) {
            ex.getStackTrace();
        } finally {
            session.close();
        }
        if (members.isEmpty())
            return false;
        for (Object mmb : members) {
            result = (String) mmb;
            if (result != null)
                return true;
            else return false;
        }
        return false;
    }


    @Override
    public Member getMemberById(int id) {
        List<Object[]> members = null;

        try {
            session.beginTransaction();
            @SuppressWarnings("JpaQlInspection")
            Query queryMemberById =
                    session.createQuery
                            (" select m.id,m.firstName,m.secondName,m.lastName,m.birth" +
                                    ",m.ensembleName,m.countOfMembers,m.gender" +
                                    ",m.office,m.address,m.passport,m.INN,m.boss,m.category,m.firstSong" +
                                    ",m.secondSong,m.registration,m.turnNumber from Member m where m.id = :paramID");
            queryMemberById.setParameter("paramID", id);
            members = queryMemberById.list();
            session.getTransaction().commit();
        } catch (TransactionException ex) {
            ex.getStackTrace();
        } finally {
            session.close();
        }

        for (Object[] result : members) {
            return BuilderMember.getBuilderMember()
                    .setId((Integer) result[0])
                    .setFirstName((String) result[1])
                    .setSecondName((String) result[2])
                    .setLastName((String) result[3])
                    .setBirth((Date) result[4])
                    .setEnsembleName((String) result[5])
                    .setCountOfMembers((Integer) result[6])
                    .setGender((Gender) result[7])
                    .setOffice((String) result[8])
                    .setAddress(getAddressById((Integer) result[9]))
                    .setPassport((String) result[10])
                    .setINN((String) result[11])
                    .setBoss((String) result[12])
                    .setCategory(getCategoryByName((String) result[13]))
                    .setFirstSong(getSongById((Integer) result[14]))
                    .setSecondSong(getSongById((Integer) result[15]))
                    .setRegistration((Boolean) result[16])
                    .setTurnNumber((Integer) result[17])
                    .build();

        }
        return null;
    }

    private Address getAddressById(int id) {

        List<Object[]> addresses = null;
        //noinspection JpaQlInspection
        Query queryAddresses = session.createQuery("select a.id,a.country,a.region,a.district,a.city,a.phone From Address a");
        addresses = queryAddresses.list();
        for (Object[] address : addresses) {
            if ((Integer) address[0] == id)
                return BuilderAddress.getBuilderAddress().setId(id)
                        .setCountry((String) address[1])
                        .setRegion((String) address[2])
                        .setDistrict((String) address[3])
                        .setCity((String) address[4])
                        .setPhone((String) address[5])
                        .build();
        }
        return null;


    }

    @Override
    public boolean isMemberAlreadyEvaluated(String juryUserName, int memberId, int songNumber) {
        //смотрим в таблицу Оценок(марк)
        //берем все оценки, отбираем по жюри, мемберу и номеру песни. Если такая запись есть,то тру
        session.beginTransaction();
        List<Mark> memberMarks = null;
        //noinspection JpaQlInspection
        Query queryMemberMarks = session.createQuery("From Mark m where m.jury = :paramJury and m.member = :paramMemberID and m.song = :paramSongId");
        queryMemberMarks.setParameter("paramJury", juryUserName);
        queryMemberMarks.setParameter("paramMemberID", memberId);
        queryMemberMarks.setParameter("paramSongId", songNumber);
        memberMarks = queryMemberMarks.list();
        session.getTransaction().commit();
        return !memberMarks.isEmpty();

    }

    @Override
    public List<Category> getAllCategoryFromDB() {
        List<Category> result;
        session.beginTransaction();
        //noinspection JpaQlInspection
        result = session.createQuery("from Category").list();
        session.getTransaction().commit();
        return result;

    }

    @Override
    public synchronized boolean saveMark(Member member, User jury, MARKCRITERIA markcriteria, Song song, int value) {
        // сохраняем оценку в таблицу Марк,если все ок то ТРУ
        session.beginTransaction();
        Mark mark = BuilderMark.getNewBuilderMark()
                .setMember(member)
                .setJury(jury)
                .setCriteriaOfMark(markcriteria)
                .setSong(song)
                .setValue(value).build();

        try {
            session.beginTransaction();
            session.save(mark);
            session.getTransaction().commit();
        } catch (HibernateException ex) {

            ex.getStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Song getSongById(int id) {
        Song result = new Song();
        result.setId(id);
        List<Object> songs = null;
        //noinspection JpaQlInspection
        Query querySongs = session.createQuery("select s.name From Song s where s.id = :paramId");
        querySongs.setParameter("paramId", id);
        songs = querySongs.list();

        for (Object song : songs) {
            result.setName((String) song);
            return result;
        }
        return null;

    }

    @Override
    public synchronized int getFreeIdOfMarkDB() {
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
        List<MARKCRITERIA> result;
        session.beginTransaction();
        //noinspection JpaQlInspection
        result = session.createQuery("from MARKCRITERIA").list();
        session.getTransaction().commit();
        return result;

    }

    @Override
    public synchronized boolean deleteMemberFromDBById(int idMember) {
        return false;
    }

    @Override
    public synchronized boolean updateMember(Member notUpdatedMember, Member updatedMember) {
        return false;
    }

    @Override
    public synchronized boolean updateAddress(Address oldAddress, Address newAddress) {
        return false;
    }

    @Override
    public synchronized boolean updateSongName(Song oldSong, Song newSong) {
        return false;
    }

   /* @Override
    public boolean isSongAlreadyEvaluatedByJury(Song song, User jury) {

        session.beginTransaction();
        //noinspection JpaQlInspection
        Query queryMarks = session.createQuery("From Mark m where m.song = :paramSongId and m.jury = :paramJuryId");
        queryMarks.setParameter("paramSongId", song.getId());
        queryMarks.setParameter("paramJuryId", jury.getId());
        List marks = queryMarks.list();
        session.getTransaction().commit();
        return !marks.isEmpty();
    }*/

    @Override
    public synchronized boolean deleteAddressFromDBById(int idAddress) {
        return false;
    }

    @Override
    public synchronized boolean deleteSongFromDBById(int idSong) {
        return false;
    }

    @Override
    public synchronized boolean deleteMarksFromDBByMemberId(int idMember) {
        return false;
    }

    @Override
    public synchronized boolean deleteJuryFromDBByUserName(String userName) {
        return false;
    }

    @Override
    public synchronized boolean deleteMarksFromDBByJuryUserName(String userName) {
        return false;
    }


}
