package io.github.hossensyedriadh.cryptography.service.decoding;

import io.github.hossensyedriadh.cryptography.enumerator.EncodingType;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public final class DecodingServiceImpl implements DecodingService {
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
            return new String(Base64.getDecoder().decode(message.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        } else if (type == EncodingType.UNICODE) {
            String string = message.split(" ")[0];
            string = string.replace("\\", "");
            String[] array = string.split("u");
            StringBuilder text = new StringBuilder();

            for (int i = 1; i < array.length; i += 1) {
                int hex = Integer.parseInt(array[i], 16);
                text.append((char) hex);
            }

            return text.toString();
        } else if (type == EncodingType.URL) {
            return URLDecoder.decode(message, StandardCharsets.UTF_8);
        }
        throw new RuntimeException("Invalid input");
    }
}
