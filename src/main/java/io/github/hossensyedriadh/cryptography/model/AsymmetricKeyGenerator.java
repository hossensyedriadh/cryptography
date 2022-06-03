package io.github.hossensyedriadh.cryptography.model;

import io.github.hossensyedriadh.cryptography.enumerator.KeySize;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.KeyPair;
import java.util.Base64;

@NoArgsConstructor
@Getter
@Setter
public class AsymmetricKeyGenerator {
    @Setter(AccessLevel.NONE)
    private String publicKey;

    @Setter(AccessLevel.NONE)
    private String privateKey;

    private KeySize keySize;

    @Setter(AccessLevel.NONE)
    private KeyPair keyPair;

    public AsymmetricKeyGenerator(KeyPair keyPair) {
        this.publicKey = "-----BEGIN PUBLIC KEY-----\n".concat(Base64.getEncoder()
                .encodeToString(keyPair.getPublic().getEncoded())).concat("\n-----END PUBLIC KEY-----\n");
        this.privateKey = "-----BEGIN PRIVATE KEY-----\n".concat(Base64.getEncoder()
                .encodeToString(keyPair.getPrivate().getEncoded())).concat("\n-----END PRIVATE KEY-----\n");
    }
}
