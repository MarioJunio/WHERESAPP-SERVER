package test;

import org.junit.Test;
import util.Key;

/**
 * Created by MarioJ on 08/04/15.
 */
public class KeyTest {

    public void generateKeyHashed() {

        String encoded = Key.encode("55", "3491162895");
        String decoded = Key.decode(encoded.getBytes());
        String hashed = Key.generate("55", "3491162895");

        System.out.println("Original: " + "553491162895");
        System.out.println("Encoded: " + encoded);
        System.out.println("Decoded: " + decoded);

        System.out.println("Crypt 128 bits: " + hashed);
        System.out.println("Length " + hashed.length());

    }

    @Test
    public void generateConfirmCode() {
        System.out.println(Key.generateConfirmCode());
    }

}
