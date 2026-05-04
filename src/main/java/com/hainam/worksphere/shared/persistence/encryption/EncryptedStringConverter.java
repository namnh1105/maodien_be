package com.hainam.worksphere.shared.persistence.encryption;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Converter
@Slf4j
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    private static AesGcmStringEncryptor encryptor;

    @Autowired
    public void setEncryptor(AesGcmStringEncryptor encryptor) {
        EncryptedStringConverter.encryptor = encryptor;
        log.debug("EncryptedStringConverter initialized with encryptor");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) {
            return attribute;
        }
        if (encryptor == null) {
            log.warn("EncryptedStringConverter: Encryptor not initialized, returning plain text");
            return attribute;
        }
        try {
            return encryptor.encrypt(attribute);
        } catch (Exception e) {
            log.error("Encryption failed for attribute: {}", e.getMessage());
            return attribute;
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return dbData;
        }
        if (encryptor == null) {
            log.warn("EncryptedStringConverter: Encryptor not initialized, returning raw data");
            return dbData;
        }
        try {
            return encryptor.decrypt(dbData);
        } catch (Exception e) {
            log.error("Decryption failed for data: {}", e.getMessage());
            return dbData; // Fallback to raw data
        }
    }
}
