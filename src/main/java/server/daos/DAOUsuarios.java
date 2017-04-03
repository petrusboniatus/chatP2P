package server.daos;

import api.IServer.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import javafx.beans.property.Property;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;

/**
 * Created by pedro on 3/04/17.
 */
public class DAOUsuarios {

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
        return resultado > 0;
    }

    public Profile getUsuario(String nombre){
        return datastore.find(Profile.class).field("nombre").equal(nombre).get();
    }
    public Profile getUsuario(Profile usuario){
        return datastore.find(Profile.class).field("nombre").equal(usuario.getName()).get();
    }
    public void insertarUsuario(Profile nuevoPerfil){
        datastore.save(nuevoPerfil);
    }

    public void actualizarUsuario(Profile nuevoPerfil){
        datastore.save(nuevoPerfil);
    }

    public void anhadirAmigo(Profile amigo1, Profile amigo2 ){

        datastore.findAndModify(
            datastore.createQuery(Profile.class).field("nombre").equal(amigo1.getName()),
            datastore.createUpdateOperations(Profile.class).addToSet("amigos",amigo2.getName())
        );

        datastore.findAndModify(
                datastore.createQuery(Profile.class).field("nombre").equal(amigo2.getName()),
                datastore.createUpdateOperations(Profile.class).addToSet("amigos",amigo1.getName())
        );
    }

    public void anhadirPeticion(Profile amigoEnviador, Profile amigoRecibidor ){

        datastore.findAndModify(
                datastore.createQuery(Profile.class).field("nombre").equal(amigoRecibidor.getName()),
                datastore.createUpdateOperations(Profile.class).addToSet("peticionesPendientes",amigoEnviador.getName())
        );

    }

    public void borrarPeticion(Profile amigoEnviador, Profile amigoRecibidor ){

        List<String> nuevaListaPeticiones = datastore.get(Profile.class,amigoRecibidor.getName()).getPeticionesPendientes();
        nuevaListaPeticiones.remove(amigoEnviador.getName());
        datastore.findAndModify(
                datastore.createQuery(Profile.class).field("nombre").equal(amigoRecibidor.getName()),
                datastore.createUpdateOperations(Profile.class).set("peticionesPendientes",nuevaListaPeticiones)
        );

    }

    public void borrarAmigo(Profile amigo1, Profile amigo2 ){

        List<String> nuevaListaAmigos = datastore.get(Profile.class,amigo2.getName()).getAmigos();
        nuevaListaAmigos.remove(amigo1.getName());
        datastore.findAndModify(
                datastore.createQuery(Profile.class).field("nombre").equal(amigo1.getName()),
                datastore.createUpdateOperations(Profile.class).set("amigos",nuevaListaAmigos)
        );

        nuevaListaAmigos = datastore.get(Profile.class,amigo1.getName()).getAmigos();
        nuevaListaAmigos.remove(amigo2.getName());
        datastore.findAndModify(
                datastore.createQuery(Profile.class).field("nombre").equal(amigo2.getName()),
                datastore.createUpdateOperations(Profile.class).set("amigos",nuevaListaAmigos)
        );

    }

    public List<Profile> getAmigos(Profile perfil){
        Profile perfilActual = datastore.get(Profile.class,perfil.getName());

        List<Profile> resultado = datastore.get(Profile.class, perfilActual.getAmigos()).asList();
        return resultado;
    }

    public List<Profile> getPeticionesPendientes(Profile perfil){
        Profile perfilActual = datastore.get(Profile.class,perfil.getName());

        List<Profile> resultado = datastore.get(Profile.class, perfilActual.getPeticionesPendientes()).asList();
        return resultado;
    }

    public List<Profile> buscarUsuarios(String busqueda){
        return datastore.createQuery(Profile.class).field("nombre").contains(busqueda).asList();
    }

}
