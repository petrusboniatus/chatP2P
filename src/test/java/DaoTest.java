import org.junit.Test;
import server.daos.DAOUsuarios;
import server.daos.Profile;

import static org.junit.Assert.*;

/**
 * Created by pedro on 3/04/17.
 */
public class DaoTest {

    @Test
    public void doTest(){

        DAOUsuarios daoUsuarios = new DAOUsuarios();
        Profile p1 = new Profile();
        Profile p2 = new Profile();
        Profile p3 = new Profile();
        p1.setName("p1");
        p2.setName("p2");
        p3.setName("p3");


        daoUsuarios.inicializar();
        daoUsuarios.insertarUsuario(p1);
        daoUsuarios.insertarUsuario(p2);
        daoUsuarios.insertarUsuario(p3);
        daoUsuarios.anhadirAmigo(p2,p3);
        daoUsuarios.anhadirPeticion(p2,p3);
        p1.setOnline(true);
        daoUsuarios.actualizarUsuario(p1);

        assertTrue(daoUsuarios.existeUsuario(p1.getName()));
        assertFalse(daoUsuarios.existeUsuario("Esto no es un usuario"));
        assertEquals(daoUsuarios.getAmigos(p2).get(0),p3);
        assertEquals(daoUsuarios.getPeticionesPendientes(p3).get(0),p2);
        assertEquals(daoUsuarios.getUsuario(p1.getName()),p1);
        assertEquals(daoUsuarios.getUsuario(p1),p1);
        assertEquals(daoUsuarios.buscarUsuarios("p").size(),3);

        daoUsuarios.borrarAmigo(p2,p3);
        daoUsuarios.borrarPeticion(p2,p3);
        assertEquals(daoUsuarios.getAmigos(p2).size(),0);
        assertEquals(daoUsuarios.getPeticionesPendientes(p3).size(),0);
    }
}
