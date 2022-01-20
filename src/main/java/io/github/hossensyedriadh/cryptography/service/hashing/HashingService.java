package io.github.hossensyedriadh.cryptography.service.hashing;

import io.github.hossensyedriadh.cryptography.enumerator.HashingAlgorithm;

public sealed interface HashingService permits HashingServiceImpl {
    /**
     * Generate cryptographic hash using the defined algorithm
     *
     * @param algorithm Name of the algorithm
     * @param message   message to be hashed
     * @return Generated hash
     * @see HashingAlgorithm
     */
    String generateHash(String message, HashingAlgorithm algorithm);
}
