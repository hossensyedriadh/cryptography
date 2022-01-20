package io.github.hossensyedriadh.cryptography.enumerator;

public enum KeySize {
    RSA_1024 (1024),
    RSA_2048 (2048),
    RSA_4096 (4096);

    private final int value;

    KeySize(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
