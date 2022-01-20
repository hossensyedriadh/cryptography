package io.github.hossensyedriadh.cryptography.service.encryption;

import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;
import java.util.Map;

public sealed interface SymmetricEncryptionService permits SymmetricEncryptionServiceImpl {
    /**
     * Generate Symmetric Key
     *
     * @param password Password to generate secret with
     * @return Generated secret key along with Format and Algorithm
     * @see javax.crypto.SecretKey
     */
    SecretKey generateKey(@NonNull String password);

    /**
     * Generate Symmetric Key
     *
     * @param password Password to generate secret with
     * @param salt     Salt to generate secret with
     * @return Generated secret key along with Format and Algorithm
     * @see javax.crypto.SecretKey
     */
    SecretKey generateKey(@NonNull String password, @NonNull String salt);

    /**
     * Encrypt message with given password
     *
     * @param message  Message to be encrypted
     * @param password Password to generate secret and encrypt message
     * @return Encrypted message with required information for decryption
     */
    Map<String, Object> encryptMessage(@NonNull String message, @NonNull String password);

    /**
     * Encrypt message with given password, salt
     *
     * @param message  Message to be encrypted
     * @param password Password to generate secret and encrypt message
     * @param salt     Salt to generate secret and encrypt message
     * @return Encrypted message with required information for decryption
     */
    Map<String, Object> encryptMessage(@NonNull String message, @NonNull String password, @NonNull String salt);

    /**
     * Decrypt message with given key, parameter spec, salt
     *
     * @param message  Encrypted message
     * @param password Password to decrypt message
     * @param param    IVParameterSpec value
     * @param salt     Salt to decrypt message
     * @see javax.crypto.spec.IvParameterSpec
     */
    String decryptMessage(@NonNull String message, @NonNull String password, @NonNull String param, @NonNull String salt);
}
