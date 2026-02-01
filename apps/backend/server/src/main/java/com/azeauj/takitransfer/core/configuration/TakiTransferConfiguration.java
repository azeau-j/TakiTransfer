package com.azeauj.takitransfer.core.configuration;

import com.azeauj.takitransfer.config.TakiTransferProperties;
import com.azeauj.takitransfer.storage.StorageProvider;
import io.github.wasabithumb.jtoml.JToml;
import io.github.wasabithumb.jtoml.document.TomlDocument;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class TakiTransferConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TakiTransferProperties getApplicationProperties() throws IOException {
        File configFile = new File("/etc/takitransfer/conf.toml");
        InputStream inputStream;

        if (configFile.exists()) {
            inputStream = new FileInputStream(configFile);
        } else {
            inputStream = new ClassPathResource("conf.toml").getInputStream();
        }

        String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        JToml toml = JToml.jToml();
        TomlDocument doc = toml.readFromString(content);

        String storageSystem = doc.get("storage_system").asPrimitive().asString();

        try {
            return new TakiTransferProperties(storageSystem, doc);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Primary
    public StorageProvider getSystemStorage(TakiTransferProperties transferProperties, List<StorageProvider> storageProviders, Logger logger) {
        logger.info("{} system storage available.", storageProviders.size());
        StorageProvider storageProvider = storageProviders.stream()
                .filter(storage -> storage.getSystemName().equals(transferProperties.storageSystem()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Storage system not found for " + transferProperties.storageSystem()));

        logger.info("System Storage found : {}", storageProvider.getSystemName());

        storageProvider.setProperties(transferProperties);
        return storageProvider;
    }
}