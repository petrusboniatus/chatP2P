import org.junit.Assert;
import org.junit.Test;
import server.daos.DAOUsuarios;
import server.daos.Profile;

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

        Assert.assertTrue(daoUsuarios.existeUsuario(p1.getName()));
        Assert.assertFalse(daoUsuarios.existeUsuario("Esto no es un usuario"));
        Assert.assertEquals(daoUsuarios.getAmigos(p2).get(0),p3);
        Assert.assertEquals(daoUsuarios.getPeticionesPendientes(p3).get(0),p2);
        Assert.assertEquals(daoUsuarios.getUsuario(p1.getName()),p1);
        Assert.assertEquals(daoUsuarios.getUsuario(p1),p1);
        Assert.assertEquals(daoUsuarios.buscarUsuarios("p").size(),3);

        daoUsuarios.borrarAmigo(p2,p3);
        daoUsuarios.borrarPeticion(p2,p3);
        Assert.assertEquals(daoUsuarios.getAmigos(p2).size(),0);
        Assert.assertEquals(daoUsuarios.getPeticionesPendientes(p3).size(),0);
    }


}
