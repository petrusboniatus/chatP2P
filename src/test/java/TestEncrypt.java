import org.junit.Assert;
import org.junit.Test;
import server.utils.Security;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class TestEncrypt {

    @Test
    public void saltCheck() {
        String hash = Security.encrypt("12345");

        String encrypted = "OmKmnrrM2ocvrKqYEbiwnUWTdebMYpMpkMS2KMgbshGp79RC/scdr98VVO/8ua0PFyrPdfGyn1Qi\n" +
                "JGFmbNTwFw==$3DV9crfxPrnE3VJem2gnIB9Wp+xyHHo2b2yGrd+VaalKPV7pZZaRediuggYf8eB/zUXsmEIxzt9G\n" +
                "tS5p9VWANw==";

        Assert.assertNotEquals(encrypted, hash);
    }

    @Test
    public void check() {
        String hash = Security.encrypt("12345");
        Assert.assertTrue(Security.checkPassword("12345", hash));
        Assert.assertFalse(Security.checkPassword("12346", hash));
    }
}
