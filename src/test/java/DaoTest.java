import org.junit.Assert;
import org.junit.Test;
import server.daos.DAO;
import server.daos.Profile;

import java.util.ArrayList;

/**
 * Created by pedro on 3/04/17.
 */
public class DaoTest {

    @Test
    public void doTest(){

        DAO dao = new DAO();
        Profile p = new Profile();
        Profile p1 = new Profile();
        Profile p2 = new Profile();
        p.setName("p1");
        p1.setName("p2");
        p2.setName("p3");


        dao.inicializar();
        dao.insertarUsuario(p);
        dao.insertarUsuario(p1);
        dao.insertarUsuario(p2);


        Assert.assertTrue(dao.existeUsuario(p.getName()));
        Assert.assertFalse(dao.existeUsuario("Esto no es un usuario"));


        dao.anhadirAmigo(p1,p2);


        Assert.assertEquals(dao.getAmigos(p1).get(0),p2);


    }


}
