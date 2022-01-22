package io.github.hossensyedriadh.cryptography.service.encryption;

import io.github.hossensyedriadh.cryptography.enumerator.SymmetricKeyAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Class implementing methods defined in SymmetricEncryptionService interface
 * <p>
 * Currently, only AES_128_GCM and AES_256_GCM algorithms are supported.
 */
@Service
public final class SymmetricEncryptionServiceImpl implements SymmetricEncryptionService {
    private static final String AES = "AES";
    private static final String AES_GCM_NoPadding = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH_BITS = 128;

    /**
     * Generate Symmetric Key
     *
     * @param algorithm Name of the algorithm
     * @return Generated secret key along with Format and Algorithm
     * @see SecretKey
     */
    @Override
    public SecretKey generateKey(SymmetricKeyAlgorithm algorithm) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(algorithm.getValue());

            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypt message with given password
     *
     * @param message Message to be encrypted
     * @param key     Key to generate secret and encrypt message
     * @return Encrypted message with required information for decryption
     */
    @Override
    public Map<String, Object> encryptMessage(String message, String key) {
        Map<String, Object> encryptionInfo = new HashMap<>();
        try {
            Cipher cipher = Cipher.getInstance(AES_GCM_NoPadding);
            byte[] decodedKey = Base64.getDecoder().decode(key);
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, AES);

            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), AES);

            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            secureRandom.nextBytes(iv);

            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);

            byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            String encryptedMessage = Base64.getEncoder().encodeToString(encryptedBytes);
            AlgorithmParameters parameters = cipher.getParameters();

            encryptionInfo.put("message", encryptedMessage);
            encryptionInfo.put("iv", Base64.getEncoder().encodeToString(iv));

            return encryptionInfo;
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException
                | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypt message with given key, initialization vector, salt
     *
     * @param message Encrypted message
     * @param key     Key to decrypt message
     * @param iv      Initialization Vector
     * @see IvParameterSpec
     */
    @Override
    public String decryptMessage(String message, String key, String iv) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(key);
            byte[] initVector = Base64.getDecoder().decode(iv);

            Cipher cipher = Cipher.getInstance(AES_GCM_NoPadding);
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, AES);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), AES);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, initVector);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(message.getBytes(StandardCharsets.UTF_8)));

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException
                | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
