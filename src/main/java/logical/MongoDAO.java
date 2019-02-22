package logical;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entity.User;
import org.bson.Document;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MongoDAO extends Repository{
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> users;
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

        boolean creation = true;
        for (String name : database.listCollectionNames()
        ) {
            if (name.equals("users")) {
                //   System.out.println(name);
                creation = false;
                break;
            }
        }
        if (creation) {
            database.createCollection("users");
        } else {
            System.out.println("users in " + database.getName() + " database already exists.");
        }

        users = database.getCollection("users");
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

 /*   public boolean isUserAlreadyRegistered(String userName) {
        boolean result = false;
        Document d1 = users.find(new Document("userName", userName)).first();
        if (d1 != null) {
            result = true;
        }
        return result;
    }
*/
    public boolean isUserAlreadyRegistered(User user) {
        boolean result = false;
        Document d1 = users.find(new Document("userName", user.getUserName())).first();
        if (d1 != null) {
            result = true;
        }
        return result;
    }

/*
    public boolean isPasswordRight(String userName, String pass) {
        boolean result = false;
        Document d1 = users.find(new Document("userName", userName)).first();

        if (d1 != null) {
            if (d1.getString("password").equals(pass)) {
                result = true;
            }
        }
        return result;
    }*/

    public boolean isPasswordRight(User user) {
        boolean result = false;
        Document d1 = users.find(new Document("userName", user.getUserName())).first();
        if (d1 != null) {
            if (d1.getString("password").equals(user.getPassword())) {
                result = true;
            }
        }
        return result;
    }
/*
    public boolean registerUser(String userName, String password) {
        boolean result = false;
        try {
            users.insertOne(new Document("userName", userName)
                    .append("password", password)
                    .append("role", "USER"));
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
*/
  /*
    public boolean registerUser(User user) {
        boolean result = false;
        try {
            users.insertOne(new Document("userName", user.getUserName())
                    .append("password", user.getPassword())
                    .append("role", user.getRole())
            );

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
*/
    public String getROLE(User user) {
        Document d1 = users.find(new Document("userName", user.getUserName())).first();
        if (d1 == null) {
            return "UNKNOWN";
        }
        return (String) d1.get("role");
    }


    public List<User> getAllUsersFromDB() {
        List<User> result = new ArrayList<>();
        for (Document doc : users.find()
        ) {
            User user = new User(doc.getString("userName"),
                    doc.getString("password"),
                    doc.getString("firstName"),
                    doc.getString("secondName"),
                    doc.getString("lastName"),
                    doc.getString("office")
                    );
            user.setRole(doc.getString("role"));

            result.add(user);
        }
        return result;

    }

    public User getUserByName(String name) {
        Document user = users.find(new Document("userName", name)).first();
        User result = new User(user.getString("userName"),
                                user.getString("password"),
                user.getString("firstName"),
                user.getString("secondName"),
                user.getString("lastName"),
                user.getString("office"));
        result.setRole(user.getString("role"));
        return result;
    }

}
