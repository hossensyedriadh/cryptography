package io.github.hossensyedriadh.cryptography.service.encryption;

import io.github.hossensyedriadh.cryptography.enumerator.SymmetricKeyAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * Class implementing methods defined in SymmetricEncryptionService interface
 * <p>
 * Currently, only AES_128_GCM and AES_256_GCM algorithms are supported.
 */
@Service
public final class SymmetricEncryptionServiceImpl implements SymmetricEncryptionService {
    private static final String ALGORITHM = "AES";
    private static final String AES_VARIANT = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH_BITS = 128;
    private static final int NONCE_LENGTH_BYTE = 16;

    private static byte[] generateNonce(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static String generateStrongPassword(int length) {
        String chars = "#:@%!_([')~]<=*{^;,&?$.->/+}";

        String random = RandomStringUtils.random((length * 90) / 100);
        String s = RandomStringUtils.random(((length * 10) / 100), chars);
        String temp = random.concat(s);

        return shuffle(temp);
    }

    private static String shuffle(String string) {
        List<String> res = Arrays.asList(string.split(""));
        Collections.shuffle(res, new SecureRandom());

        return String.join("", res);
    }

    /**
     * Generate secret using specified algorithm
     *
     * @param algorithm Algorithm to be used
     * @return Encrypted message with required information for decryption
     */
    @Override
    public SecretKey generateKey(SymmetricKeyAlgorithm algorithm) {
        try {
            byte[] salt = generateNonce(NONCE_LENGTH_BYTE);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            KeySpec keySpec;

            if (algorithm == SymmetricKeyAlgorithm.AES_128_GCM) {
                char[] password = generateStrongPassword(26).toCharArray();
                keySpec = new PBEKeySpec(password, salt, 65536, 128);
            } else if (algorithm == SymmetricKeyAlgorithm.AES_256_GCM) {
                char[] password = generateStrongPassword(36).toCharArray();
                keySpec = new PBEKeySpec(password, salt, 65536, 256);
            } else {
                throw new RuntimeException("Algorithm not found");
            }

            return new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), ALGORITHM);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate secret using specified algorithm, password and salt
     *
     * @param algorithm Algorithm to be used
     * @param password  Key to generate secret and encrypt message
     * @param salt      Salt/Nonce to add in encryption stage
     * @return Encrypted message with required information for decryption
     */
    @Override
    public SecretKey generateKey(SymmetricKeyAlgorithm algorithm, char[] password, byte[] salt) {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            KeySpec keySpec;

            if (algorithm == SymmetricKeyAlgorithm.AES_128_GCM) {
                keySpec = new PBEKeySpec(password, salt, 65536, 128);
            } else if (algorithm == SymmetricKeyAlgorithm.AES_256_GCM) {
                keySpec = new PBEKeySpec(password, salt, 65536, 256);
            } else {
                throw new RuntimeException("Algorithm not found");
            }

            return new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), ALGORITHM);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypt message with provided key
     *
     * @param message Message to be encrypted
     * @param key     Key to be used to encrypt message
     * @return Encrypted message with required information for decryption
     */
    @Override
    public String encryptMessage(String message, String key) {
        boolean isBase64 = org.apache.tomcat.util.codec.binary.Base64.isBase64(key);

        try {
            byte[] salt = generateNonce(NONCE_LENGTH_BYTE);
            byte[] iv = generateNonce(GCM_IV_LENGTH);

            SecretKey aesKey;

            if (isBase64) {
                int keyLength = Base64.getDecoder().decode(key).length * 8;
                if (keyLength == 128) {
                    aesKey = generateKey(SymmetricKeyAlgorithm.AES_128_GCM, key.toCharArray(), salt);
                } else if (keyLength == 256) {
                    aesKey = generateKey(SymmetricKeyAlgorithm.AES_256_GCM, key.toCharArray(), salt);
                } else {
                    throw new RuntimeException("Invalid AES Key");
                }
            } else {
                aesKey = generateKey(SymmetricKeyAlgorithm.AES_256_GCM, key.toCharArray(), salt);
            }

            Cipher cipher = Cipher.getInstance(AES_VARIANT);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv));

            byte[] cipherText = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            byte[] encryptedText = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                    .put(iv).put(salt).put(cipherText).array();

            return Base64.getEncoder().encodeToString(encryptedText);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException
                 | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypt message with provided key
     *
     * @param message Encrypted message
     * @param key     Key to decrypt message
     * @see IvParameterSpec
     */
    @Override
    public String decryptMessage(String message, String key) {
        boolean isBase64 = org.apache.tomcat.util.codec.binary.Base64.isBase64(key);

        try {
            byte[] decoded = Base64.getDecoder().decode(message.getBytes(StandardCharsets.UTF_8));
            ByteBuffer buffer = ByteBuffer.wrap(decoded);

            byte[] iv = new byte[GCM_IV_LENGTH];
            buffer.get(iv);

            byte[] salt = new byte[NONCE_LENGTH_BYTE];
            buffer.get(salt);

            byte[] cipherText = new byte[buffer.remaining()];
            buffer.get(cipherText);

            SecretKey secretKey;

            if (isBase64) {
                int keyLength = Base64.getDecoder().decode(key).length * 8;

                if (keyLength == 128) {
                    secretKey = generateKey(SymmetricKeyAlgorithm.AES_128_GCM, key.toCharArray(), salt);
                } else if (keyLength == 256) {
                    secretKey = generateKey(SymmetricKeyAlgorithm.AES_256_GCM, key.toCharArray(), salt);
                } else {
                    throw new RuntimeException("Invalid AES Key");
                }
            } else {
                secretKey = generateKey(SymmetricKeyAlgorithm.AES_256_GCM, key.toCharArray(), salt);
            }

            Cipher cipher = Cipher.getInstance(AES_VARIANT);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv));

            byte[] plainText = cipher.doFinal(cipherText);

            return new String(plainText, StandardCharsets.UTF_8);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException
                 | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
