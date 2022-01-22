package io.github.hossensyedriadh.cryptography.service.decoding;

import io.github.hossensyedriadh.cryptography.enumerator.EncodingType;

public interface DecodingService {
    /**
     * Encode message of defined format
     *
     * @param message Message to be decoded
     * @param type    Current format of the message
     * @return Decoded message
     * @see EncodingType
     */
    String decode(String message, EncodingType type);
}
