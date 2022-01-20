package io.github.hossensyedriadh.cryptography.enumerator;

public enum HashingAlgorithm {
    MD5("MD5"),
    SHA_1("SHA-1"),
    SHA_2_256("SHA-256"),
    SHA_2_384("SHA-384"),
    SHA_2_512("SHA-512"),
    SHA_3_256("SHA3-256"),
    SHA_3_384("SHA3-384"),
    SHA_3_512("SHA3-512"),
    BCRYPT ("BCrypt");

    private final String value;

    HashingAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
