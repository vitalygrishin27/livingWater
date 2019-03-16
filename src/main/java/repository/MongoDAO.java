package repository;

import authentication.Authentication;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entity.*;
import org.bson.Document;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MongoDAO extends Repository {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> adminMongoCollection;
    private static MongoCollection<Document> juryMongoCollection;
    private static MongoCollection<Document> roleMongoCollection;
    private static MongoCollection<Document> memberMongoCollection;
    private static MongoCollection<Document> addressMongoCollection;
    private static MongoCollection<Document> songMongoCollection;
    private static MongoCollection<Document> markMongoCollection;
    private static MongoCollection<Document> markCriteriaMongoCollection;
    private static MongoCollection<Document> categoryMongoCollection;


    private static Document document;
    private static MongoDAO mongoDAO = null;
    private static boolean single;

    public static Repository getDAO() {
        if (single) {
            return mongoDAO;
        }
        return new MongoDAO();
    }

    public static void main(String[] args) {
        MongoDAO mongoDAO = new MongoDAO();
    }


    private MongoDAO() {
        Properties properties = readPropertiesForDB("mongo.properties");
        single = true;
        mongoClient = new MongoClient(properties.getProperty("hostMongo"), Integer
                .valueOf(properties.getProperty("port")));
        database = mongoClient.getDatabase(properties.getProperty("database"));

        juryMongoCollection = database.getCollection("jury");
        adminMongoCollection = database.getCollection("admin");
        roleMongoCollection = database.getCollection("role");
        memberMongoCollection = database.getCollection("member");
        addressMongoCollection = database.getCollection("address");
        songMongoCollection = database.getCollection("song");
        markMongoCollection = database.getCollection("mark");
        markCriteriaMongoCollection = database.getCollection("MarkCriteria");
        categoryMongoCollection = database.getCollection("category");


        document = new Document();
        mongoDAO = this;
    }

    private static Properties readPropertiesForDB(String filename) {
        Properties properties = new Properties();
        File file = new File(filename);
        String g = file.getAbsolutePath();
        if (!new File(filename).exists()) {
            System.out.println("Properties file do not exist.");
            System.exit(1);
        } else {


            try (FileReader fileReader = new FileReader(filename)) {
                properties.load(fileReader);

            } catch (IOException e) {
                System.out.println("Error with reading file properties.");
                e.printStackTrace();
            }
        }
        return properties;

    }

    public boolean isUserAlreadyRegistered(User user) {
        boolean result = false;


        Document d1 = juryMongoCollection.find(new Document("userName", user.getUserName())).first();
        Document d2 = adminMongoCollection.find(new Document("userName", user.getUserName())).first();

        if (d1 != null || d2 != null) {
            result = true;
        }
        return result;
    }


    public boolean isPasswordRight(User user) {
        boolean result = false;
        Document d1 = juryMongoCollection.find(new Document("userName", user.getUserName())).first();
        Document d2 = adminMongoCollection.find(new Document("userName", user.getUserName())).first();

        if (d1 != null) {
            if (d1.getString("password").equals(user.getPassword())) {
                result = true;
            }
        } else if (d2 != null) {
            if (d2.getString("password").equals(user.getPassword())) {
                result = true;
            }
        }
        return result;
    }

    public String getROLE(User user) {
        Document d1 = juryMongoCollection.find(new Document("userName", user.getUserName())).first();
        if (d1 == null) {
            return "UNKNOWN";
        }
        return (String) d1.get("role");
    }


    public List<User> getAllFromDBByRole(Role role) {
        List<User> result = new ArrayList<>();
        MongoCollection<Document> currentMongoCollection = null;
        if (role.getName().equals("ADMIN")) currentMongoCollection = adminMongoCollection;
        if (role.getName().equals("MANAGER")) currentMongoCollection = adminMongoCollection;
        if (role.getName().equals("JURY")) currentMongoCollection = juryMongoCollection;

        for (Document doc : currentMongoCollection.find()
        ) {

            User user = new User(doc.getString("userName"),
                    doc.getString("password"),
                    doc.getString("firstName"),
                    doc.getString("secondName"),
                    doc.getString("lastName"),
                    doc.getString("office"),
                    role
            );
            result.add(user);
        }
        return result;

    }


    public User getJuryByUserName(String userName) {
        User result = null;
        Document user = juryMongoCollection.find(new Document("userName", userName)).first();
        //  if (user == null) user = adminMongoCollection.find(new Document("userName", userName)).first();

        if (user != null) {
            Role role = new Role(user.getInteger("roleId"), roleMongoCollection
                    .find(new Document("id", user.get("roleId"))).first().getString(userName));

            result = new User(user.getString("userName"),
                    user.getString("password"),
                    user.getString("firstName"),
                    user.getString("secondName"),
                    user.getString("lastName"),
                    user.getString("office"),
                    role);
        }
        return result;
    }


  /*  private User getJuryById(Integer id) {
        User result = null;
        Document user = juryMongoCollection.find(new Document("id", id)).first();
        if (user != null) {
            return getJuryByUserName(user.getString("userName"));
        }
        return result;
    }
*/

    public User getAdminByUserName(String userName) {
        User result = null;
        Document user = adminMongoCollection.find(new Document("userName", userName)).first();
        //  if (user == null) user = adminMongoCollection.find(new Document("userName", userName)).first();

        if (user != null) {
            Role role = new Role(user.getInteger("roleId"), roleMongoCollection
                    .find(new Document("id", user.get("roleId"))).first().getString(userName));

            result = new User(user.getString("userName"),
                    user.getString("password"),
                    user.getString("firstName"),
                    user.getString("secondName"),
                    user.getString("lastName"),
                    user.getString("office"),
                    role);
        }
        return result;
    }


    @Override
    public List<Role> getAllRolesFromDB() {
        List<Role> result = new ArrayList<>();
        for (Document doc : roleMongoCollection.find()
        ) {
            Role role = new Role(doc.getInteger("id"),
                    doc.getString("name"));
            result.add(role);
        }
        return result;
    }

    @Override
    public List<Member> getAllMembersFromDB() {
        List<Member> result = new ArrayList<>();
        for (Document doc : memberMongoCollection.find()
        ) {
            result.add(getMemberById(doc.getInteger("id")));

        }
        return result;
    }

    @Override
    public Member getMemberById(Integer id) {
        Member result = null;
        Document doc = memberMongoCollection.find(new Document("id", id)).first();
        if (doc != null) {
            return BuilderMember.getBuilderMember()
                    .setId(doc.getInteger("id"))
                    .setFirstName(doc.getString("firstName"))
                    .setSecondName(doc.getString("secondName"))
                    .setLastName(doc.getString("lastName"))
                    .setBirth(doc.getDate("birth"))
                    .setEnsembleName(doc.getString("ensembleName"))
                    .setCountOfMembers(doc.getInteger("countOfMembers"))
                    .setGender(Gender.getGenderByChar(doc.getString("gender")))
                    .setAddress(getAddressById(doc.getInteger("addressId")))
                    .setPassport(doc.getString("passport"))
                    .setINN(doc.getString("INN"))
                    .setBoss(doc.getString("boss"))
                    .setCategory(getCategoryByName(doc.getString("category")))
                    .setFirstSong(getSongById(doc.getInteger("firstSongId")))
                    .setSecondSong(getSongById(doc.getInteger("secondSongId")))
                    .setRegistration(doc.getBoolean("registration"))
                    .setTurnNumber(doc.getInteger("turnNumber"))
                    .build();
        }
        return result;

    }

    public Category getCategoryByName(String name) {
        Document doc = categoryMongoCollection.find(new Document("name", name)).first();
        return new Category(doc.getInteger("id"), doc.getString("name"));
    }


    @Override
    public synchronized boolean saveNewMemberIntoDB(Member member) {
        //Сохраняем Адресс и получаем реальный ID в случае если он успел занятся
        member.getAddress().setId(saveAddressIntoDB(member.getAddress()));
        //Сохраняем SONG и получаем реальный ID в случае если он успел занятся
        member.getFirstSong().setId(saveSongIntoDB(member.getFirstSong()));
        member.getSecondSong().setId(saveSongIntoDB(member.getSecondSong()));
        //Проверяем ID turnNumber
        member.setTurnNumber(Authentication.getRepository().getFreeTurnNumberFromMemberDB());

        //Проверяем не занят ли уже ID
        int id = Authentication.getRepository().getFreeIdOfMembersDB();
        if (id != member.getId()) {
            System.out.println("ID of Member was CHANGED.");
        }
        member.setId(id);
        memberMongoCollection.insertOne(new Document("id", id)
                .append("lastName", member.getLastName())
                .append("firstName", member.getFirstName())
                .append("secondName", member.getSecondName())
                .append("birth", member.getBirth())
                .append("ensembleName", member.getEnsembleName())
                .append("countOfMembers", member.getCountOfMembers())
                .append("gender", member.getGender().toString())
                .append("addressId", member.getAddress().getId())
                .append("passport", member.getPassport())
                .append("INN", member.getINN())
                .append("boss", member.getBoss())
                .append("category", member.getCategory().toString())
                .append("firstSongId", member.getFirstSong().getId())
                .append("secondSongId", member.getSecondSong().getId())
                .append("registration", member.isRegistration())
                .append("turnNumber", member.getTurnNumber()));

        return true;
    }

    // возвращает id
    private synchronized int saveAddressIntoDB(Address address) {
        int id = Authentication.getRepository().getFreeIdOfAddressDB();
        if (id != address.getId()) {
            System.out.println("ID of Address was CHANGED.");
        }
        address.setId(id);
        addressMongoCollection.insertOne(new Document("id", address.getId())
                .append("country", address.getCountry())
                .append("region", address.getRegion())
                .append("district", address.getDistrict())
                .append("city", address.getCity())
                .append("phone", address.getPhone()));

        System.out.println("Address successful saved into DB. (" + address.getId() + ").");
        return id;
    }


    //возвращает id
    private synchronized int saveSongIntoDB(Song song) {
        int id = Authentication.getRepository().getFreeIdOfSongDB();
        if (id != song.getId()) {
            System.out.println("ID of Song was CHANGED.");
        }
        song.setId(id);
        songMongoCollection.insertOne(new Document("id", song.getId())
                .append("name", song.getName()));
        System.out.println("Song successful saved into DB. (" + song.getId() + " " + song.getName() + ").");
        return id;

    }


    private Address getAddressById(int id) {
        Document doc = addressMongoCollection.find(new Document("id", id)).first();
        if (doc != null) {
            return BuilderAddress.getBuilderAddress().setId(id)
                    .setCountry(doc.getString("country"))
                    .setRegion(doc.getString("region"))
                    .setDistrict(doc.getString("district"))
                    .setCity(doc.getString("city"))
                    .setPhone(doc.getString("phone"))
                    .build();
        } else {
            return null;
        }

    }

    private Song getSongById(int id) {
        Song result = new Song();

        Document doc = songMongoCollection.find(new Document("id", id)).first();

        result.setId(id);
        result.setName(doc.getString("name"));
        return result;


    }

    @Override
    public List<Mark> getAllMarksFromDB() {
        List<Mark> result = new ArrayList<>();

        for (Document element : markMongoCollection.find()
        ) {
            result.add(getMarkById(element.getInteger("id")));
        }
        return result;


    }

    private Mark getMarkById(Integer id) {
        Mark result = null;
        Document element = markMongoCollection.find(new Document("id", id)).first();
        if (element != null) {
            return BuilderMark.getNewBuilderMark().setId(element.getInteger("id"))
                    .setJury(getJuryByUserName(element.getString("juryUserName")))
                    .setMember(getMemberById(element.getInteger("juryId")))
                    .setCriteriaOfMark(MARKCRITERIA.getMarkCriteriaByName(element.getString("markCriteria")))
                    .setSong(getSongById(element.getInteger("songId")))
                    .setValue(element.getInteger("value"))
                    .build();

        }
        return result;


    }


    @Override
    public Role createRoleByName(String name) {
        Document doc = roleMongoCollection.find(new Document("name", "JURY")).first();
        return new Role(doc.getInteger("id"), doc.getString("name"));
    }

    @Override
    public synchronized int getFreeIdOfJuryDB() {
        List<Integer> usedId = new ArrayList<>();

        for (Document doc : juryMongoCollection.find()
        ) {
            usedId.add(doc.getInteger("id"));
        }

        if (usedId.size() == 0) {
            return 1;
        } else {
            Collections.sort(usedId);

            return (usedId.get(usedId.size() - 1)) + 1;
        }

    }

    @Override
    public synchronized int getFreeIdOfMembersDB() {
        List<Integer> usedId = new ArrayList<>();

        for (Document doc : memberMongoCollection.find()
        ) {
            usedId.add(doc.getInteger("id"));
        }

        if (usedId.size() == 0) {
            return 1;
        } else {
            Collections.sort(usedId);

            return (usedId.get(usedId.size() - 1)) + 1;
        }

    }

    @Override
    public synchronized int getFreeIdOfAddressDB() {
        List<Integer> usedId = new ArrayList<>();

        for (Document doc : addressMongoCollection.find()
        ) {
            usedId.add(doc.getInteger("id"));
        }

        if (usedId.size() == 0) {
            return 1;
        } else {
            Collections.sort(usedId);

            return (usedId.get(usedId.size() - 1)) + 1;
        }

    }

    @Override
    public synchronized int getFreeIdOfSongDB() {
        List<Integer> usedId = new ArrayList<>();

        for (Document doc : songMongoCollection.find()
        ) {
            usedId.add(doc.getInteger("id"));
        }

        if (usedId.size() == 0) {
            return 1;
        } else {
            Collections.sort(usedId);

            return (usedId.get(usedId.size() - 1)) + 1;
        }

    }

    @Override
    public synchronized int getFreeTurnNumberFromMemberDB() {
        List<Integer> usedId = new ArrayList<>();

        for (Document doc : memberMongoCollection.find()
        ) {
            usedId.add(doc.getInteger("turnNumber"));
        }

        if (usedId.size() == 0) {
            return 1;
        } else {
            Collections.sort(usedId);

            return (usedId.get(usedId.size() - 1)) + 1;
        }
    }

    @Override
    public boolean isLoginForNewJuryCorrect(String login) {
        if(juryMongoCollection.find(new Document("userName",login)).first()!=null){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public synchronized boolean saveNewJuryIntoDB(User jury) {
       juryMongoCollection.insertOne(new Document("id", getFreeIdOfJuryDB())
       .append("userName",jury.getUserName())
       .append("password",jury.getPassword())
       .append("firstName",jury.getFirstName())
       .append("secondName",jury.getSecondName())
       .append("lastName",jury.getLastName())
       .append("office",jury.getOffice())
       .append("roleId",jury.getRole().getId()));

        return true;
    }

    @Override
    public boolean isMemberSoloByMemberId(int id) {
        Document doc=memberMongoCollection.find(new Document("id",id)).first();
        if(doc.getInteger("countOfMembers")==1){
            return true;
        }else {
            return false;
        }
    }

    /*  @Override
    public List<Member> getListOfMembers() {
        List<Member> result = new ArrayList<>();

        for (Document element : memberMongoCollection.find()
        ) {
            BuilderMember.getBuilderMember().setId(element.getInteger("id"))
                                            .setLastName(element.getString("lastName"))
                                            .setFirstName(element.getString("firstName"))
                                            .setSecondName(element.getString("secondName"))
                                            .setBirth(element.getDate("birth"))
                                            .setEnsembleName(element.getString("ensembleName"))
                                            .setCountOfMembers(element.getInteger("countOfMembers"))
                                            .setGender(Gender.getGenderByChar(element.getString("gender")))
                                            .setAddress(getAddressById())

        }

new Gender("M");

        member.countOfMembers=builderMember.getCountOfMembers();
        member.gender=builderMember.getGender();
        member.address=builderMember.getAddress();
        member.passport=builderMember.getPassport();
        member.INN=builderMember.getINN();
        member.boss=builderMember.getBoss();
        member.category =builderMember.getCategory();
        member.firstSong=builderMember.getFirstSong();
        member.secondSong=builderMember.getSecondSong();
        member.registration=builderMember.isRegistration();
        member.turnNumber=builderMember.getTurnNumber();








        return null;
    }*/
}
