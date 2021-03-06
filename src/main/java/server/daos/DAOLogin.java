package server.daos;

import api.Defaults;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by pedro on 3/04/17.
 */
public class DAOLogin {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private Datastore datastore;


    public void inicializar() {
        // To connect to mongodb server
        mongoClient = new MongoClient(Defaults.databaseHost, Defaults.databasePort);

        // Nos bajamos una BD
        mongoDatabase = mongoClient.getDatabase("usersBD");

        //Creamos la instancia de morphia (es el parser que recomienda mongo). Le tenemos que pasar nuestro modelo
        //para que lo mapee y haga su magia
        Morphia morphia = new Morphia();
        morphia.getMapper().getOptions().setStoreEmpties(true);
        morphia.mapPackage("server.daos");
        datastore = morphia.createDatastore(mongoClient, "usersBD");
        datastore.ensureIndexes();
    }


    public void setCryptedPass(String usuario, String cryptedPass) {
        datastore.save(new CryptedPass(usuario, cryptedPass));
    }

    public String getCryptedPass(String usuario) {

        CryptedPass pass = datastore.createQuery(CryptedPass.class).field("usuario").equal(usuario).get();

        if (pass != null) {
            return pass.getEncryptedPass();
        } else {
            return null;
        }
    }


}
