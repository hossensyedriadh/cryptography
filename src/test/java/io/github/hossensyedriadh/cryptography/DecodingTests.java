package io.github.hossensyedriadh.cryptography;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

@SpringBootTest
class DecodingTests {
    public static final Logger logger = Logger.getLogger(DecodingTests.class.getName());

    @Test
    void converts_base64_to_text() {
        String encoded = "RW5jb2RlIHRoaXM=";
        String originalText = "Encode this";
        String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);

        assert decoded.equals(originalText);

        logger.info(">>>>> Decoded: " + decoded);
    }

    @Test
    void converts_unicode_to_bengali() {
        String unicode = "\\u09ac\\u09be\\u0982\\u09b2\\u09be\\u09a6\\u09c7\\u09b6";

        String originalString = "বাংলাদেশ";

        String string = unicode.split(" ")[0];
        string = string.replace("\\", "");
        String[] array = string.split("u");
        StringBuilder text = new StringBuilder();

        for (int i = 1; i < array.length; i += 1) {
            int hex = Integer.parseInt(array[i], 16);
            text.append((char) hex);
        }

        assert text.toString().equals(originalString);

        logger.info(">>>>> Decoded: " + text);
    }

    @Test
    void decodes_url() {
        String encodedUrl = "https%3A%2F%2Fcryptography-demo.herokuapp.com";
        String decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);

        String originalUrl = "https://cryptography-demo.herokuapp.com";

        assert decodedUrl.equals(originalUrl);

        logger.info(">>>>> Decoded: " + decodedUrl);
    }
}
