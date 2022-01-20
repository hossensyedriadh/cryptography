package io.github.hossensyedriadh.cryptography.model;

import io.github.hossensyedriadh.cryptography.enumerator.HashingAlgorithm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class HashModel {
    private String text;
    private String hash;
    private HashingAlgorithm algorithm;
}
