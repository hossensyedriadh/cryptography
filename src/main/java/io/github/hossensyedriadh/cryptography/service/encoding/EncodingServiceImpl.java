package io.github.hossensyedriadh.cryptography.service.encoding;

import io.github.hossensyedriadh.cryptography.enumerator.EncodingType;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public final class EncodingServiceImpl implements EncodingService {
    /**
     * Encode message into defined format
     *
     * @param message Message to be encoded
     * @param type    Type of Encoding
     * @return Encoded message
     * @see EncodingType
     */
    @Override
    public String encode(String message, EncodingType type) {
        if (type == EncodingType.BASE_64) {
            String encodedMessage = Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
            return encodedMessage;
        }
        throw new RuntimeException("Invalid encoding type");
    }

    /**
     * Encode message of defined format
     *
     * @param message Message to be decoded
     * @param type    Current format of the message
     * @return Decoded message
     * @see EncodingType
     */
    @Override
    public String decode(String message, EncodingType type) {
        if (type == EncodingType.BASE_64) {
            String decodedMessage = new String(Base64.getDecoder().decode(message.getBytes(StandardCharsets.UTF_8)));
            return decodedMessage;
        }
        throw new RuntimeException("Invalid input");
    }
}
