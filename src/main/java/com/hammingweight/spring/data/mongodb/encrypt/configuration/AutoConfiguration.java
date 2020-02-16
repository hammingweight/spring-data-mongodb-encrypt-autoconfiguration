package com.hammingweight.spring.data.mongodb.encrypt.configuration;

import com.bol.crypt.CryptVault;
import com.bol.secure.CachedEncryptionEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
@ConditionalOnProperty("hammingweight.spring.data.mongodb.encrypt.key")
/**
 * Autoconfigures Spring Data Encryption for MongoDB if the Configuration property
 * hammingweight.spring.data.mongodb.encrypt.key contains a 128 bit AES key encoded
 * in base64.
 */
public class AutoConfiguration {

    // A 128-bit AES key encoded in base64.
    @Value("${hammingweight.spring.data.mongodb.encrypt.key}")
    private String secretKeyInBase64;

    // Flag indicating whether to ignore decryption failures.
    @Value("${hammingweight.spring.data.mongodb.encrypt.silent-decryption-failures:false}")
    private boolean silentDecryptionFailures;

    @Bean
    CryptVault cryptVault() throws Exception {
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKeyInBase64);
        return new CryptVault()
                .with256BitAesCbcPkcs5PaddingAnd128BitSaltKey(0, secretKeyBytes)
                .withDefaultKeyVersion(0);
    }

    @Bean
    CachedEncryptionEventListener cachedEncryptionEventListener(CryptVault cryptVault) {
        return new CachedEncryptionEventListener(cryptVault)
                .withSilentDecryptionFailure(silentDecryptionFailures);
    }
}
