package repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entity.*;
import org.bson.Document;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
            result.add(BuilderMember.getBuilderMember()
                                    .setId(doc.getInteger("id"))
                                    .setFirstName(doc.getString("firstName"))
                                    .setSecondName(doc.getString("secondName"))
                                    .setLastName(doc.getString("lastName"))
                                    .setBirth(doc.getDate("birth"))
                                    .setEnsembleName(doc.getString("ensembleName"))
                                    .setCountOfMembers(doc.getInteger("countOfMembers"))
                                    .setGender(Gender.getGenderById(doc.getString("gender")))
                                    .setAddress(getAddressById(doc.getInteger("addressId")))
                                    .setPassport(doc.getString("passport"))
                                    .setINN(doc.getString("INN"))
                                    .setBoss(doc.getString("boss"))
                                    .setCategory(Category.getCategoryByName(doc.getString("category")))
                                    .setFirstSong(getSongById(doc.getInteger("firstSongId")))
                                    .setSecondSong(getSongById(doc.getInteger("secondSongId")))
                                    .setRegistration(doc.getBoolean("registration"))
                                    .setTurnNumber(doc.getInteger("turnNumber"))
                                    .build());

   }
        return result;
    }

    @Override
    public Address getAddressById(int id) {
        Address result = new Address();
        Document doc = addressMongoCollection.find(new Document("id", id)).first();
        result.setId(id);
        result.setCountry(doc.getString("city"));
        result.setRegion(doc.getString("region"));
        result.setDistrict(doc.getString("district"));
        result.setCity(doc.getString("city"));
        result.setPhone(doc.getString("phone"));
        return result;
    }

    @Override
    public Song getSongById(int id) {
        Song result = new Song();

        Document doc = songMongoCollection.find(new Document("id", id)).first();

        result.setId(id);
        result.setName(doc.getString("name"));
        return result;


    }
}
