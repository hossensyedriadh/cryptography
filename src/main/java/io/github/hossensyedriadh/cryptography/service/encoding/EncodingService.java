package io.github.hossensyedriadh.cryptography.service.encoding;

import io.github.hossensyedriadh.cryptography.enumerator.EncodingType;

public sealed interface EncodingService permits EncodingServiceImpl {
    /**
     * Encode message into defined format
     *
     * @param message Message to be encoded
     * @param type    Type of Encoding
     * @return Encoded message
     * @see EncodingType
     */
    String encode(String message, EncodingType type);
}
