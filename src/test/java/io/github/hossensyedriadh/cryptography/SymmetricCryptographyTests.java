package io.github.hossensyedriadh.cryptography;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.logging.Logger;

@SpringBootTest
class SymmetricCryptographyTests {
    public static final Logger logger = Logger.getLogger(SymmetricCryptographyTests.class.getName());

    @Test
    void generates_new_aes_gcm_key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);

            SecretKey secretKey = keyGenerator.generateKey();
            assert secretKey != null;

            //verifying 256 bit length key
            assert secretKey.getEncoded().length == 32;

            assert secretKey.getAlgorithm().equals("AES");

            String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            logger.info(">>>>> Key: " + key);
        } catch (NoSuchAlgorithmException e) {
            logger.warning(e.getMessage());
        }
    }

    @Test
    void encrypts_text_using_provided_aes_key() {
        try {
            String key = "scdPsnzcQZUo/N/4xzSSBfkJhr6eu/DyIUKzFKZlnk4=";

            String message = "Encrypt me";

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] decodedKey = Base64.getDecoder().decode(key);
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

            byte[] iv = new byte[12];
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            secureRandom.nextBytes(iv);

            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);

            byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

            String encryptedMessage = Base64.getEncoder().encodeToString(encryptedBytes);
            AlgorithmParameters parameters = cipher.getParameters();

            String initVector = Base64.getEncoder().encodeToString(iv);

            logger.info(">>>>> encrypted message: " + encryptedMessage);
            logger.info(">>>>> initialization vector: " + initVector);
            logger.info(">>>>> algorithm: " + secretKeySpec.getAlgorithm() + "_256_" + parameters.getAlgorithm());

        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException
                | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            logger.warning(e.getMessage());
        }
    }

    @Test
    void decrypts_message_using_provided_key_and_init_vector() {
        String message = "7JcVtOidZom8p8b1xAuSPGG5NqwFES5gTzw=";
        String key = "scdPsnzcQZUo/N/4xzSSBfkJhr6eu/DyIUKzFKZlnk4=";
        String iv = "lrzdDYqtIscJAUAC";

        String originalMessage = "Encrypt me";

        try {
            byte[] decodedKey = Base64.getDecoder().decode(key);
            byte[] initVector = Base64.getDecoder().decode(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, initVector);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(message.getBytes(StandardCharsets.UTF_8)));

            String decryptedMessage = new String(decrypted, StandardCharsets.UTF_8);

            assert decryptedMessage.equals(originalMessage);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException
                | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            logger.warning(e.getMessage());
        }
    }
}
