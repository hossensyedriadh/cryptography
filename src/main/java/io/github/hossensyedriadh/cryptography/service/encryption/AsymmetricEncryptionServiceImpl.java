package io.github.hossensyedriadh.cryptography.service.encryption;

import io.github.hossensyedriadh.cryptography.enumerator.KeySize;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public final class AsymmetricEncryptionServiceImpl implements AsymmetricEncryptionService {
    private static final String RSA_ALGORITHM = "RSA";

    /**
     * Generate Public-Private Keypair of the defined keySize
     *
     * @param keySize Size of the RSA Keypair to be generated
     * @return Generated Public-Private keypair
     * @see KeyPair
     * @see KeySize
     */
    @Override
    public KeyPair generateKeyPair(KeySize keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            keyPairGenerator.initialize(keySize.getValue(), secureRandom);

            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypt message using Public Key from the generated Public-Private keypair with defined keySize
     *
     * @param message Message to be encrypted
     * @param keySize Size of the RSA Keypair to be generated
     * @return Encrypted message along with Keypair
     * @see PublicKey
     * @see PrivateKey
     * @see KeySize
     */
    @Override
    public Map<String, Object> encryptMessage(String message, KeySize keySize) {
        Map<String, Object> encryptionInfo = new HashMap<>();

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            keyPairGenerator.initialize(keySize.getValue(), secureRandom);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.PUBLIC_KEY, publicKey);
            byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            String encryptedMessage = Base64.getEncoder().encodeToString(encrypted);

            encryptionInfo.put("encryptedText", encryptedMessage);
            encryptionInfo.put("publicKey", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            encryptionInfo.put("privateKey", Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            encryptionInfo.put("keyFormat", privateKey.getFormat());
            encryptionInfo.put("algorithm", privateKey.getAlgorithm());

            return encryptionInfo;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypt message using given Public key
     *
     * @param message   Message to be encrypted
     * @param publicKey Public key value (Base64 encoded)
     * @return Encrypted message
     * @see PublicKey
     */
    @Override
    public String encryptMessage(String message, String publicKey) {
        try {
            byte[] decodedPublicKey = Base64.getDecoder().decode(publicKey.getBytes(StandardCharsets.UTF_8));

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PublicKey key = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.PUBLIC_KEY, key);
            byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypt message using given Private key
     *
     * @param message    Encrypted message
     * @param privateKey Private key value (Base64 encoded)
     * @return Decrypted plain text message
     * @see PrivateKey
     */
    @Override
    public String decryptMessage(String message, String privateKey) {
        try {
            byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKey.getBytes(StandardCharsets.UTF_8));

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey key = keyFactory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.PRIVATE_KEY, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(message));

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
