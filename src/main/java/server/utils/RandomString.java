package server.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by pedro on 4/04/17.
 */
public class RandomString {

    private SecureRandom random = new SecureRandom();

    public String getString() {
        return new BigInteger(130, random).toString(32);
    }

}
