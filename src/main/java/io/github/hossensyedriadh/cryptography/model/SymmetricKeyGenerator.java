package io.github.hossensyedriadh.cryptography.model;

import io.github.hossensyedriadh.cryptography.enumerator.SymmetricKeyAlgorithm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.SecretKey;
import java.util.Base64;

@NoArgsConstructor
@Getter
@Setter
public class SymmetricKeyGenerator {
    private SymmetricKeyAlgorithm algorithm;

    @Setter(AccessLevel.NONE)
    private String key;

    public SymmetricKeyGenerator(SymmetricKeyAlgorithm algorithm, SecretKey secretKey) {
        this.algorithm = algorithm;
        this.key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
