package server.daos;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;

/**
 * Created by pedro on 3/04/17.
 */
public class DAO {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private Datastore datastore;


    public void inicializar(){
        // To connect to mongodb server
        mongoClient = new MongoClient( "localhost" , 27017 );

        // Nos bajamos una BD
        mongoDatabase = mongoClient.getDatabase("usersBD");

        //Creamos la instancia de morphia (es el parser que recomienda mongo). Le tenemos que pasar nuestro modelo
        //para que lo mapee y haga su magia
        Morphia morphia = new Morphia();
        morphia.mapPackage("server.daos");
        datastore = morphia.createDatastore(mongoClient, "usersBD");
        datastore.ensureIndexes();
    }


    public boolean existeUsuario(String nombre){

        float resultado = datastore.createQuery(Profile.class).field("nombre").equal(nombre).count();
        return resultado >0;
    }

    public void insertarUsuario(Profile nuevoPerfil){
        datastore.save(nuevoPerfil);
    }

    public void anhadirAmigo(Profile amigo1,Profile amigo2 ){

        amigo1 = datastore.createQuery(Profile.class)
                .field("nombre").equal(amigo1.getName())
                .asList().get(0);

        amigo2 = datastore.createQuery(Profile.class)
                .field("nombre").equal(amigo2.getName())
                .asList().get(0);

        amigo1.getAmigos().add(amigo2);
        amigo2.getAmigos().add(amigo1);

        datastore.save(amigo1);
        datastore.save(amigo2);


    }

    public List<Profile> getAmigos(Profile perfil){
        return  datastore.createQuery(Profile.class)
                .field("nombre")
                .equal(perfil.getName())
                .asList();
    }

}
