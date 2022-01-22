package io.github.hossensyedriadh.cryptography.enumerator;

public enum SymmetricKeyAlgorithm {
    //Currently, only AES_GCM is implemented
    AES_128_GCM(128),
    AES_256_GCM(256);

    private final int value;

    SymmetricKeyAlgorithm(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
