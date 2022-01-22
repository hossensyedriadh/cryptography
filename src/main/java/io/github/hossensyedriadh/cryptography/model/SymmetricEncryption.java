package io.github.hossensyedriadh.cryptography.model;

import io.github.hossensyedriadh.cryptography.enumerator.Method;
import io.github.hossensyedriadh.cryptography.enumerator.SymmetricKeyAlgorithm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SymmetricEncryption {
    private String plainTextMessage;

    private String encryptedMessage;

    private Method method;

    private SymmetricKeyAlgorithm algorithm;

    private String key;

    private String initializationVector;
}
