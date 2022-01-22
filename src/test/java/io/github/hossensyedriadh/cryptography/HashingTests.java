package io.github.hossensyedriadh.cryptography;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.github.hossensyedriadh.cryptography.enumerator.HashingAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;

@SpringBootTest
class HashingTests {
    public static final Logger logger = Logger.getLogger(HashingTests.class.getName());

    @Test
    void generates_bcrypt_hash_of_given_string() {
        String message = "Random message";
        try {
            String hashed = new String(BCrypt.with(SecureRandom.getInstanceStrong()).hash(10,
                    message.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

            assert hashed.startsWith("$2a$10$");
            assert hashed.length() == 60;

            logger.info(">>>>> Hash: " + hashed);
        } catch (NoSuchAlgorithmException e) {
            logger.warning(e.getMessage());
        }
    }

    @Test
    void generates_md5_hash_of_given_string() {
        String message = "Important message";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HashingAlgorithm.MD5.getValue());
            byte[] hashBytes = messageDigest.digest(message.getBytes(StandardCharsets.UTF_8));

            String hash = Base64.getEncoder().encodeToString(hashBytes);

            logger.info(">>>>> Hash: " + hash);
        } catch (NoSuchAlgorithmException e) {
            logger.warning(e.getMessage());
        }
    }

    @Test
    void generates_sha3_512_hash_of_given_string() {
        String message = "The Espionage";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HashingAlgorithm.SHA_3_512.getValue());
            byte[] hashBytes = messageDigest.digest(message.getBytes(StandardCharsets.UTF_8));

            String hash = Base64.getEncoder().encodeToString(hashBytes);

            logger.info(">>>>> Hash: " + hash);
        } catch (NoSuchAlgorithmException e) {
            logger.warning(e.getMessage());
        }
    }
}
