package io.github.hossensyedriadh.cryptography.service.encryption;

import io.github.hossensyedriadh.cryptography.enumerator.SymmetricKeyAlgorithm;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;
import java.util.Map;

/**
 * Interface defining methods to be implemented for Symmetric Encryption
 * <p>
 * Currently, only AES_128_GCM and AES_256_GCM algorithms are supported.
 */
public sealed interface SymmetricEncryptionService permits SymmetricEncryptionServiceImpl {
    /**
     * Generate Symmetric Key
     *
     * @param algorithm Name of the algorithm
     * @return Generated secret key along with Format and Algorithm
     * @see javax.crypto.SecretKey
     */
    SecretKey generateKey(@NonNull SymmetricKeyAlgorithm algorithm);

    /**
     * Encrypt message with given key
     *
     * @param message Message to be encrypted
     * @param key     Key to generate secret and encrypt message
     * @return Encrypted message with required information for decryption
     */
    Map<String, Object> encryptMessage(@NonNull String message, @NonNull String key);

    /**
     * Decrypt message with given key, initialization vector, salt
     *
     * @param message Encrypted message
     * @param key     Key to decrypt message
     * @param iv      Initialization vector
     * @see javax.crypto.spec.IvParameterSpec
     */
    String decryptMessage(@NonNull String message, @NonNull String key, @NonNull String iv);
}
