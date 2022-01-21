package io.github.hossensyedriadh.cryptography.service.encryption;

import io.github.hossensyedriadh.cryptography.enumerator.KeySize;

import java.security.KeyPair;

public sealed interface AsymmetricEncryptionService permits AsymmetricEncryptionServiceImpl {
    /**
     * Generate Public-Private Keypair of the defined keySize
     *
     * @param keySize Size of the RSA Keypair to be generated
     * @return Generated Public-Private keypair
     * @see java.security.KeyPair
     * @see KeySize
     */
    KeyPair generateKeyPair(KeySize keySize);

    /**
     * Encrypt message using given Public key
     *
     * @param message   Message to be encrypted
     * @param publicKey Public key value (Base64 encoded)
     * @return Encrypted message
     * @see java.security.PublicKey
     */
    String encryptMessage(String message, String publicKey);

    /**
     * Decrypt message using given Private key
     *
     * @param message    Encrypted message
     * @param privateKey Private key value (Base64 encoded)
     * @return Decrypted plain text message
     * @see java.security.PrivateKey
     */
    String decryptMessage(String message, String privateKey);
}
