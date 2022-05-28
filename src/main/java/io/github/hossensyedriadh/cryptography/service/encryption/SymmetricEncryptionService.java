package io.github.hossensyedriadh.cryptography.service.encryption;

import io.github.hossensyedriadh.cryptography.enumerator.SymmetricKeyAlgorithm;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;

/**
 * Interface defining methods to be implemented for Symmetric Encryption
 * <p>
 * Currently, only AES_128_GCM and AES_256_GCM algorithms are supported.
 */
public sealed interface SymmetricEncryptionService permits SymmetricEncryptionServiceImpl {
    /**
     * Generate secret using specified algorithm
     *
     * @param algorithm Algorithm to be used
     * @return Encrypted message with required information for decryption
     */
    SecretKey generateKey(@NonNull SymmetricKeyAlgorithm algorithm);

    /**
     * Generate secret using specified algorithm, password and salt
     *
     * @param algorithm Algorithm to be used
     * @param password  Key to generate secret and encrypt message
     * @param salt      Salt/Nonce to add in encryption stage
     * @return Encrypted message with required information for decryption
     */
    SecretKey generateKey(@NonNull SymmetricKeyAlgorithm algorithm, @NonNull char[] password, @NonNull byte[] salt);

    /**
     * Encrypt message with provided key
     *
     * @param message Message to be encrypted
     * @param key     Key to be used to encrypt message
     * @return Encrypted message with required information for decryption
     */
    String encryptMessage(@NonNull String message, @NonNull String key);

    /**
     * Decrypt message with provided key
     *
     * @param message Encrypted message
     * @param key     Key to decrypt message
     * @see javax.crypto.spec.IvParameterSpec
     */
    String decryptMessage(@NonNull String message, @NonNull String key);
}
