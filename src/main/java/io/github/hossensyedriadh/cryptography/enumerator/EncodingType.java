package io.github.hossensyedriadh.cryptography.enumerator;

public enum EncodingType {
    BASE_64 ("Base64");

    private final String value;

    EncodingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
