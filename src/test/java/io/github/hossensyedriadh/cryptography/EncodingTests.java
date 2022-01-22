package io.github.hossensyedriadh.cryptography;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

@SpringBootTest
class EncodingTests {
    public static final Logger logger = Logger.getLogger(EncodingTests.class.getName());

    @Test
    void converts_text_to_base64() {
        String text = "Encode this";

        String encoded = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));

        logger.info(">>>>> Base64: " + encoded);
    }

    @Test
    void converts_bengali_to_unicode() {
        String text = "বাংলাদেশ";

        char[] textChars = text.toCharArray();

        String encoded = "";

        for (char textChar : textChars) {
            encoded = encoded.concat("\\u" + Integer.toHexString(textChar | 0x10000).substring(1));
        }

        assert encoded.length() > 0;
        assert !encoded.equals(text);

        logger.info(">>>>> Unicode: " + encoded);
    }

    @Test
    void encodes_url() {
        String url = "https://cryptography-demo.herokuapp.com";

        String encoded = URLEncoder.encode(url, StandardCharsets.UTF_8);

        assert !encoded.equals(url);

        logger.info(">>>>> URL encoded: " + encoded);
    }
}
