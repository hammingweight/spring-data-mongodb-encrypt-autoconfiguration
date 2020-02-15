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
public class AutoConfiguration {

    @Value("${hammingweight.spring.data.mongodb.encrypt.key}")
    private String secretKey;

    @Bean
    public CryptVault cryptVault() throws Exception {
        byte[] vaultKey = Base64.getDecoder().decode(secretKey);
        return new CryptVault()
                .with256BitAesCbcPkcs5PaddingAnd128BitSaltKey(0, vaultKey)
                .withDefaultKeyVersion(0);
    }

    @Bean
    CachedEncryptionEventListener cachedEncryptionEventListener(CryptVault cryptVault) {
        return new CachedEncryptionEventListener(cryptVault);
    }
}