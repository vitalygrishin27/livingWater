package repository;

import entity.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import hibernateUtils.HibernateSessionFactoryUtil;
import org.hibernate.TransactionException;
import org.hibernate.query.Query;

import java.util.ArrayList;
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
        session.beginTransaction();
        Query getRoleQuery = session.createQuery("select r.name from Role r, User u where r.id = u.role.id and u.userName = :paramName");
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
        Query allRoles = session.createQuery(" from Role r ");
        List<Role> roles = allRoles.getResultList();
        return roles;

    }

    @Override
    public List<User> getAllFromDBByRole(Role role) {
        List<User> users = null;
        try {
            Query getUsersByRole = session.createQuery("select u.userName,u.password,u.firstName" +
                    ",u.secondName,u.lastName,u.office from User u  where u.role = :paramRole");
            getUsersByRole.setParameter("paramRole", role.getId());
            users = getUsersByRole.getResultList();
        } catch (HibernateException ex) {
            ex.getStackTrace();
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
        } catch (HibernateException ex) {
            ex.getStackTrace();
        }

        return null;
    }

    @Override
    public List<Member> getAllMembersFromDB() {
        List<Member> members = session.createQuery("From Member").list();
        return members;

    }

    @Override
    public User getAdminByUserName(String name) {
        return getUser(name);
    }


    @Override
    public List<Mark> getAllMarksFromDB() {
        List<Mark> marks = session.createQuery("From Mark").list();
        return marks;
    }

    @Override
    public synchronized int getFreeIdOfMembersDB() {
        List<Object> members = session.createQuery("select m.id From Member m").list();
        return getFreeNumber(members);
    }

    @Override
    public synchronized int getFreeIdOfAddressDB() {
        List<Object> addresses = session.createQuery("select  a.id From Address a").list();
        return getFreeNumber(addresses);
    }

    @Override
    public synchronized int getFreeIdOfSongDB() {
        List<Object> songs = session.createQuery("select  s.id From Song s").list();
        return getFreeNumber(songs);
    }


    @Override
    public synchronized int getFreeTurnNumberFromMemberDB() {
        List<Object> members = session.createQuery("select m.turnNumber From Member m").list();
        return getFreeNumber(members);

    }

    private synchronized int getFreeNumber(List<Object> members) {
        int max = Integer.MIN_VALUE;
        if (members.isEmpty()) {
            return 1;
        }
        for (Object memb : members) {
            int cur = (Integer) memb;
            if (cur > max) {
                max = cur;
            }
        }
        return max + 1;
    }

    @Override
    public synchronized boolean saveNewMemberIntoDB(Member member) {
        try {
            session.beginTransaction();
            session.save(member);
            session.getTransaction().commit();
        } catch (TransactionException ex) {

            ex.getStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Category getCategoryByName(String name) {
        List<Object[]> categories = null;
        try {
            Query queryCategories = session.createQuery("select c.id, c.name From Category c where c.name = :paramName");
            queryCategories.setParameter("paramName", name);
            categories = queryCategories.list();
            for (Object[] c : categories) {
                return new Category((Integer) c[0], (String) c[1]);
            }
        } catch (HibernateException e) {
            e.getStackTrace();
        }
        return null;

    }

    @Override
    public boolean isLoginForNewJuryCorrect(String login) {
        List users = null;
        try {

            Query queryUserByName = session.createQuery(" select u.userName from User u where u.userName = :paramName");
            queryUserByName.setParameter("paramName", login);
            users = queryUserByName.list();
            if (users.isEmpty())
                return true;
            else
                return false;

        } catch (HibernateException ex) {
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
        } catch (TransactionException ex) {
            ex.getStackTrace();
            return false;
        }

    }

    @Override
    public synchronized int getFreeIdOfJuryDB() {
        List<Object> jury = session.createQuery("select  u.id From User u").list();
        int min = Integer.MAX_VALUE;
        return getFreeNumber(jury);
    }

    @Override
    public Role createRoleByName(String name) {
        List<Object[]> roles = null;
        Query queryRoles = session.createQuery("select r.id, r.name From Role r where r.name = :paramName");
        queryRoles.setParameter("paramName", name);
        roles = queryRoles.list();

        for (Object[] r : roles) {
            return new Role((Integer) r[0], (String) r[1]);
        }
        return null;
    }

    @Override
    public boolean isMemberSoloByMemberId(int id) {
        List<Object> members = null;
        String result = null;
        try {
            Query queryMembers = session.createQuery(" select m.ensembleName from Member m where m.id = :id");
            queryMembers.setParameter("id", id);
            members = queryMembers.list();
        } catch (HibernateException ex) {
            ex.getStackTrace();
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
            Query queryMemberById =
                    session.createQuery
                            (" select m.id,m.firstName,m.secondName,m.lastName,m.birth" +
                                    ",m.ensembleName,m.countOfMembers,m.gender" +
                                    ",m.office,m.address,m.passport,m.INN,m.boss,m.category,m.firstSong" +
                                    ",m.secondSong,m.registration,m.turnNumber from Member m where m.id = :paramID");
            queryMemberById.setParameter("paramID", id);
            members = queryMemberById.list();
        } catch (HibernateException ex) {
            ex.getStackTrace();
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

        List<Mark> memberMarks = null;
        Query queryMemberMarks = session.createQuery("From Mark m where m.jury = :paramJury and m.member = :paramMemberID and m.song = :paramSongId");
        queryMemberMarks.setParameter("paramJury", juryUserName);
        queryMemberMarks.setParameter("paramMemberID", memberId);
        queryMemberMarks.setParameter("paramSongId", songNumber);
        memberMarks = queryMemberMarks.list();
        return !memberMarks.isEmpty();

    }

    @Override
    public List<Category> getAllCategoryFromDB() {
        List<Category> result;
        result = session.createQuery("from Category").list();
        return result;

    }

    @Override
    public synchronized boolean saveMark(Member member, User jury, MARKCRITERIA markcriteria, Song song, int value) {
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
        } catch (TransactionException ex) {

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
        List<Object> marks = session.createQuery("select m.id From Mark m").list();
        return getFreeNumber(marks);
    }

    @Override
    public List<Mark> getListOfMarksBySong(Song song) {
        List<Mark> result = null;
        Query queryMarks = session.createQuery("select m.id,m.jury,m.member,m.criteriaOfMark,m.song,m.value  From Mark m where m.song = :paramId");
        queryMarks.setParameter("paramId", song);
        List<Object[]> marks = queryMarks.list();
        for (Object[] m : marks) {
            result.add(BuilderMark.getNewBuilderMark()
                    .setId((Integer) m[0])
                    .setJury(getJuryByUserName((String) m[1]))
                    .setMember(getMemberById((Integer) m[2]))
                    .setCriteriaOfMark(MARKCRITERIA.getMarkCriteriaByName((String) m[3]))
                    .setSong(song)
                    .setValue((Integer) m[4])
                    .build());

        }
        return result;
    }

    @Override
    public List<Member> getMembersByCategory(Category category) {
        List<Member> result = new ArrayList<>();
        for (Member memberElement : getAllMembersFromDB()
        ) {
            if (memberElement.getCategory().equals(category)) {
                result.add(memberElement);
            }
        }
        return result;
    }

    @Override
    public List<Mark> getMarksByMember(Member member) {
        List<Mark> result = new ArrayList<>();
        for (Mark markElement : getAllMarksFromDB()
        ) {
            if (markElement.getMember().equals(member)) {
                result.add(markElement);
            }
        }
        return result;
    }

    @Override
    public List<MARKCRITERIA> getAllMarkCriteria() {
        List<MARKCRITERIA> result = null;
        result.add(MARKCRITERIA.VOCAL);
        result.add(MARKCRITERIA.REPERTOIRE);
        result.add(MARKCRITERIA.ARTISTIC);
        result.add(MARKCRITERIA.INDIVIDUALY);
        return result;

    }



}
