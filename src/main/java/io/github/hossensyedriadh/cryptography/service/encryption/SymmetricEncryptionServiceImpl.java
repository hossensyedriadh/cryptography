package io.github.hossensyedriadh.cryptography.service.encryption;

import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public final class SymmetricEncryptionServiceImpl implements SymmetricEncryptionService {
    private static final String AES = "AES";
    private static final String PBKDF2HmacSHA256 = "PBKDF2WithHmacSHA256";
    private static final String AES_CBC_PKCS5Padding = "AES/CBC/PKCS5Padding";
    private static final int iterationCount = 65536;
    private static final int keyLength = 256;

    /**
     * Generate Symmetric Key
     *
     * @param password Password to generate secret with
     * @return Generated secret key along with Format and Algorithm
     * @see SecretKey
     */
    @Override
    public SecretKey generateKey(String password) {
        try {
            KeyGenerator saltGenerator = KeyGenerator.getInstance(AES);
            String salt = Base64.getEncoder().encodeToString(saltGenerator.generateKey().getEncoded());

            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2HmacSHA256);
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), iterationCount, keyLength);
            SecretKey secret = secretKeyFactory.generateSecret(keySpec);

            return new SecretKeySpec(secret.getEncoded(), AES);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate Symmetric Key
     *
     * @param password Password to generate secret with
     * @param salt     Salt to generate secret with
     * @return Generated secret key along with Format and Algorithm
     * @see SecretKey
     */
    @Override
    public SecretKey generateKey(String password, String salt) {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2HmacSHA256);
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), iterationCount, keyLength);
            SecretKey secret = secretKeyFactory.generateSecret(keySpec);

            return new SecretKeySpec(secret.getEncoded(), AES);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypt message with given password
     *
     * @param message  Message to be encrypted
     * @param password Password to generate secret and encrypt message
     * @return Encrypted message with required information for decryption
     */
    @Override
    public Map<String, Object> encryptMessage(String message, String password) {
        Map<String, Object> encryptionInfo = new HashMap<>();
        try {
            KeyGenerator saltGenerator = KeyGenerator.getInstance(AES);
            String salt = Base64.getEncoder().encodeToString(saltGenerator.generateKey().getEncoded());

            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2HmacSHA256);
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), iterationCount, keyLength);
            SecretKey temp = secretKeyFactory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(temp.getEncoded(), AES);

            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5Padding);
            cipher.init(Cipher.ENCRYPT_MODE, secret);

            AlgorithmParameters parameters = cipher.getParameters();
            byte[] iv = parameters.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            String encryptedMessage = Base64.getEncoder().encodeToString(encrypted);

            encryptionInfo.put("encryptedMessage", encryptedMessage);
            encryptionInfo.put("salt", salt);
            encryptionInfo.put("ivParamSpec", Base64.getEncoder().encodeToString(iv));
            encryptionInfo.put("algorithm", parameters.getAlgorithm());

            return encryptionInfo;
        } catch (NoSuchPaddingException | IllegalBlockSizeException | InvalidKeySpecException | NoSuchAlgorithmException
                | BadPaddingException | InvalidKeyException | InvalidParameterSpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypt message with given password, salt
     *
     * @param message  Message to be encrypted
     * @param password Password to generate secret and encrypt message
     * @param salt     Salt to generate secret and encrypt message
     * @return Encrypted message with required information for decryption
     */
    @Override
    public Map<String, Object> encryptMessage(String message, String password, String salt) {
        Map<String, Object> encryptionInfo = new HashMap<>();
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2HmacSHA256);
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), iterationCount, iterationCount);
            SecretKey temp = secretKeyFactory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(temp.getEncoded(), AES);

            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5Padding);
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters parameters = cipher.getParameters();
            byte[] iv = parameters.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            String encryptedMessage = Base64.getEncoder().encodeToString(encrypted);

            encryptionInfo.put("encryptedMessage", encryptedMessage);
            encryptionInfo.put("ivParamSpec", Base64.getEncoder().encodeToString(iv));
            encryptionInfo.put("algorithm", parameters.getAlgorithm());

            return encryptionInfo;
        } catch (NoSuchPaddingException | IllegalBlockSizeException | InvalidKeySpecException | NoSuchAlgorithmException
                | BadPaddingException | InvalidKeyException | InvalidParameterSpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypt message with given key, parameter spec, salt
     *
     * @param message  Encrypted message
     * @param password Password to decrypt message
     * @param param    IVParameterSpec value
     * @param salt     Salt to decrypt message
     * @see IvParameterSpec
     */
    @Override
    public String decryptMessage(String message, String password, String param, String salt) {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2HmacSHA256);
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), iterationCount, iterationCount);
            SecretKey temp = secretKeyFactory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(temp.getEncoded(), AES);

            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5Padding);
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(Base64.getDecoder().decode(param.getBytes(StandardCharsets.UTF_8))));

            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(message.getBytes(StandardCharsets.UTF_8)));

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException
                | InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
