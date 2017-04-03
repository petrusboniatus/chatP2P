package server.DAOs;

import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;

/**
 * Created by pedro on 3/04/17.
 */

@Indexes(
        @Index(value = "nombre", fields = @Field("nombre"))
)
public class Profile {

    @Id
    private String nombre;
    @Reference("catalog")
    private ArrayList<Profile> amigos;
    @Reference("catalog")
    private ArrayList<Profile> peticionesPendientes;

}
