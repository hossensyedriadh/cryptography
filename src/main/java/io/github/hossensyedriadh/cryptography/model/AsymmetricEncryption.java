package io.github.hossensyedriadh.cryptography.model;

import io.github.hossensyedriadh.cryptography.enumerator.KeySize;
import io.github.hossensyedriadh.cryptography.enumerator.Method;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.KeyPair;
import java.util.Base64;

@NoArgsConstructor
@Getter
@Setter
public class AsymmetricEncryption {
    private String plainText;

    private String encryptedText;

    private Method method;

    private String existingKey;

    private KeySize keySize;

    @Setter(AccessLevel.NONE)
    private String genPublicKey;

    @Setter(AccessLevel.NONE)
    private String genPrivateKey;

    public AsymmetricEncryption(KeyPair keyPair) {
        this.genPublicKey = "-----BEGIN RSA PUBLIC KEY-----\n".concat(Base64.getEncoder()
                .encodeToString(keyPair.getPublic().getEncoded())).concat("\n-----END RSA PUBLIC KEY-----\n");
        this.genPrivateKey = "-----BEGIN RSA PRIVATE KEY-----\n".concat(Base64.getEncoder()
                .encodeToString(keyPair.getPrivate().getEncoded())).concat("\n-----END RSA PRIVATE KEY-----\n");
    }
}
