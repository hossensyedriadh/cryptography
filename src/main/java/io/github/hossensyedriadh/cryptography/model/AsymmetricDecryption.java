package io.github.hossensyedriadh.cryptography.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AsymmetricDecryption {
    private String encryptedText;

    private String decryptedText;

    private String privateKey;
}
