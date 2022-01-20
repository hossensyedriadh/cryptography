package io.github.hossensyedriadh.cryptography.service.hashing;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.github.hossensyedriadh.cryptography.enumerator.HashingAlgorithm;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public final class HashingServiceImpl implements HashingService {
    /**
     * Generate cryptographic hash using the defined algorithm
     *
     * @param message   message to be hashed
     * @param algorithm Name of the algorithm
     * @return Generated hash
     * @see HashingAlgorithm
     */
    @Override
    public String generateHash(String message, HashingAlgorithm algorithm) {
        try {
            if (!algorithm.equals(HashingAlgorithm.BCRYPT)) {
                MessageDigest messageDigest = MessageDigest.getInstance(algorithm.getValue());
                byte[] hashBytes = messageDigest.digest(message.getBytes(StandardCharsets.UTF_8));

                return Base64.getEncoder().encodeToString(hashBytes);
            } else {
                return new String(BCrypt.with(SecureRandom.getInstanceStrong()).hash(10, message.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
