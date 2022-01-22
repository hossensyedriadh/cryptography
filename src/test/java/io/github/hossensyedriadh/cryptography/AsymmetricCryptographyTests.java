package io.github.hossensyedriadh.cryptography;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Logger;

@SpringBootTest
class AsymmetricCryptographyTests {
    public static final Logger logger = Logger.getLogger(AsymmetricCryptographyTests.class.getName());

    @Test
    void generates_rsa_2048_key_pair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            keyPairGenerator.initialize(2048, secureRandom);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            assert keyPair.getPublic() != null;
            assert keyPair.getPrivate() != null;

            String encodedPublicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String encodedPrivateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

            logger.info(">>>>> public key: " + encodedPublicKey);
            logger.info(">>>>> private Key: " + encodedPrivateKey);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void encrypts_message_using_rsa_public_key() {
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2njreSyvlYvxJ5cxvsszTv8k" +
                "z2rqf+1dVOaWGgmRDfTdDd4/F4bY9Q/Gz2mAxPsdXil76l6oCvRwCT8Zjv+3+q/nruddCAdiDXvD5n0" +
                "nueUQ/yDYL9EYSkt1S631qp2JWm1QKRBcwVhcGkK95S3hOxOfL00ttLxguaAxoG/d1K9QwdWWhaGYYV" +
                "AQ3KPj1fJzzK3bkdfUjrUYuE24mmV+DhJf1iKGYWN8OVPLE2exNBbcjJ+Xb8Wquipq2m5zmbyMIw1eE" +
                "mmDKxRXyuFh+cIpk8tNrhKIW6DVBMFlWTS1dtZXDp9npky0G6ZIh5d4rt5Es66L60C+l5/VRwwleyc9" +
                "hQIDAQAB";

        String message = "Espionage";

        try {
            byte[] decodedPublicKey = Base64.getDecoder().decode(publicKey.getBytes(StandardCharsets.UTF_8));

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.PUBLIC_KEY, key);
            byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

            String encryptedMessage = Base64.getEncoder().encodeToString(encrypted);

            logger.info(">>>>> encrypted message: " + encryptedMessage);
        } catch (NoSuchPaddingException | InvalidKeyException | BadPaddingException | InvalidKeySpecException
                | NoSuchAlgorithmException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    @Test
    void decrypts_message_using_rsa_private_key() {
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDaeOt5LK+Vi/En" +
                "lzG+yzNO/yTPaup/7V1U5pYaCZEN9N0N3j8Xhtj1D8bPaYDE+x1eKXvqXqgK9HAJPxmO/7f6r+eu" +
                "510IB2INe8PmfSe55RD/INgv0RhKS3VLrfWqnYlabVApEFzBWFwaQr3lLeE7E58vTS20vGC5oDGg" +
                "b93Ur1DB1ZaFoZhhUBDco+PV8nPMrduR19SOtRi4TbiaZX4OEl/WIoZhY3w5U8sTZ7E0FtyMn5dv" +
                "xaq6KmrabnOZvIwjDV4SaYMrFFfK4WH5wimTy02uEohboNUEwWVZNLV21lcOn2emTLQbpkiHl3iu" +
                "3kSzrovrQL6Xn9VHDCV7Jz2FAgMBAAECggEAG3q+qYrumemUf2WT1vsBnOPYSjOZQ9aEGQf45NUD" +
                "twTv4gzslCW8t9RQbinHzd8FEmE26iOWUOxuhCc47ITrK//ArT4CNF6TXX1OBnmDC+2NTf3gW2Fa" +
                "RAwKOKbsodV3X5Wk0clyVUprKU1wS7mR3Seqa91yDIvuQaLt4w93EEdyaIX8DCSueekJPg014xZe" +
                "XJJ3Jj66yB3Pbqvv5JNC+yhi7/kVxjIAoHqs5/NiOYoajps00X+3w1YZBmb/ENerVqBtWIwaegdB" +
                "rIo3PtrmnXnDAyCF5EvzIt5Q080+kTjNxkk7iREp7Z2PRUlanKe9LeQWkNkImKy9FZTeXcj/OQKB" +
                "gQDn5MsCsmB+OpcIBqE6uq/F1t9Vtl901BCbPvlkF+p4oVYjVmOaCDacc4MF/gZmz9V+c2tlAdTX" +
                "GcCZ8CS1zw/PYfjk/8S63rH/Ki3yIlnhwS+vCWMD5hkiI3JDj9NA4TQfnQoXZDgHezW3DTPP73Nd" +
                "bT1p5s/yLwjKKcC6cWG/vQKBgQDxLvQ/BgvsXVF/b11uOS62WNlI2X6BsXpucYvXFz+GQwoTEzaQ" +
                "wZ9hzbW1V5TyQgWRGdcSLfMGA0iVP9iLVsR9RGQL2cZEenK0zrKm0Muy1jTXWMLVX2JvGVHP7Yr3" +
                "JsrEhukZf2LlY0yWBFm1fu1hoXFALKeZFxzavDdFuE8NaQKBgE7FY0iZOznlwSQSzHXcsJStttOw" +
                "zaAr4TA7JhAgJ5+ufkomsFLEc3iVrG4+qgIGm2kG3/ZBtwESWGByOKYhnic+CoQPdc5ck24v78eO" +
                "b6XGL+Jvd9j9MpBsJgZZNtOVLFWIzMLBYl74NXjm3TDHEBMuZmww4XZND9lMcZFjrmapAoGAKTPY" +
                "Thv+VXbrwVWJJJdoP4aWODfPwOBNBikntqsmEw3I3tFddQVe8RpvfHdl3q0gvlUnxNdMuS4+bCDoI" +
                "YXFM5FHdi7SDEjO58yYAJ+S7Q5jIJbtUf5llo1AyMDQCMD7yu8xS0XT41Q/dTFxEeyJKTCnZEs8po" +
                "6oQ0X8O1DtUGECgYEAxpRn5VdBnDrEjlwxVDdzuRyi32Y0qYWBFGYePrt5S9hUvLiXxBSec9B0/ge" +
                "E2YRBN39oxrPx49fAvfnmutTC4tXGtr6BEyobwyDEa8/PLvn5tFL4ypfogILIR2bCI6L9oGDiX0sU" +
                "pyGhox8onJTfBMJrxEBkw8VDkBzOdqeiKl4=";

        String encryptedMessage = "2TmeijvncFWjx20pkevc/LbDvoivzjjPQeuotYA9szBWEN2BjqtTWInyVwn" +
                "xFjotb+R+1yJ00QbXMrKID1633NZuG3JbLLmXen7rNQZmlVTMLql9zyovlaJ62ZdQtaSY5TlquVGD" +
                "amDs2JFWGt9ZEKG1cuOpKag8p5GSp521HW/ITY/JNJKoUlH5eP7uRf2qArpvWPhbJxOTXp8wGLWEe" +
                "i1Q8Kywky+1HgTe6Q8WzcCru9T4SvuaeYG7ymG4xgUfz4ZLLehqdffHTvTaX35n4wtf4Cp3UtBWot" +
                "16dN+FnEWaESz4AYhLtDuB+RnYV7jMvDtHrwGS3Nms263SgR58Ow==";

        String originalMessage = "Espionage";

        try {
            byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKey.getBytes(StandardCharsets.UTF_8));

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey key = keyFactory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.PRIVATE_KEY, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));

            String decryptedMessage = new String(decrypted, StandardCharsets.UTF_8);

            assert decryptedMessage.equals(originalMessage);

            logger.info(">>>>> decrypted message: " + decryptedMessage);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException
                | InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
