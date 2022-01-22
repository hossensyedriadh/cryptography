package io.github.hossensyedriadh.cryptography.service.encoding;

import io.github.hossensyedriadh.cryptography.enumerator.EncodingType;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
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
            return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
        } else if (type == EncodingType.UNICODE) {
            char[] messageChars = message.toCharArray();
            String encodedMessage = "";

            for (char messageChar : messageChars) {
                encodedMessage = encodedMessage.concat("\\u" + Integer.toHexString(messageChar | 0x10000).substring(1));
            }
            return encodedMessage;
        } else if (type == EncodingType.URL) {
            return URLEncoder.encode(message, StandardCharsets.UTF_8);
        }

        throw new RuntimeException("Invalid encoding type");
    }
}
