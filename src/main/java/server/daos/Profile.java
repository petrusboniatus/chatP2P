package server.daos;

import api.IServer;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedro on 3/04/17.
 */

@Indexes(
        @Index(value = "nombre", fields = @Field("nombre"))
)
public class Profile implements IServer.IProfile{


    @Id
    private String nombre;
    @Embedded
    private List<String> amigos;
    @Embedded
    private List<String> peticionesPendientes;

    boolean online;

    public Profile(){
        this.amigos = new ArrayList<>();
        this.peticionesPendientes = new ArrayList<>();
    };

    public Profile(String nombre){
        this.nombre = nombre;
        this.amigos = new ArrayList<>();
        this.peticionesPendientes = new ArrayList<>();
    };



    @Override
    public String getName() {
        return nombre;
    }

    @Override
    public boolean isConnected() {
        return online;
    }


    public boolean isOnline() {
        return online;
    }

    public List<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<String> amigos) {
        this.amigos = amigos;
    }

    public List<String> getPeticionesPendientes() {
        return peticionesPendientes;
    }

    public void setPeticionesPendientes(List<String> peticionesPendientes) {
        this.peticionesPendientes = peticionesPendientes;
    }


    public void setName(String nombre) {
        this.nombre = nombre;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        return nombre.equals(profile.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }


}
