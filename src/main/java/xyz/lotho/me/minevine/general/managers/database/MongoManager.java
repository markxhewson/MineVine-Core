package xyz.lotho.me.minevine.general.managers.database;

import com.mongodb.ConnectionString;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import org.bson.Document;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoManager {

    private final Minevine instance;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private MongoCollection<Document> users;

    public MongoManager(Minevine instance) {
        this.instance = instance;

        disableLogging();
        connect();
    }

    public void disableLogging() {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
    }

    public void connect() {
        try {
            mongoClient = MongoClients.create(new ConnectionString(this.instance.getConfiguration().getString("database.otherURI")));

            mongoDatabase = mongoClient.getDatabase("minevine");
            users = mongoDatabase.getCollection("users");
        } catch (Exception exception) {
            this.instance.getServer().getConsoleSender().sendMessage("Unable to connect to database, disabling plugin.");
            this.instance.getServer().getPluginManager().disablePlugin(this.instance);
            exception.printStackTrace();
        }
    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return this.mongoDatabase;
    }

    public MongoCollection<Document> getUsersCollection() {
        return this.users;
    }

    public void destroy() {
        this.mongoClient.close();
    }

}
