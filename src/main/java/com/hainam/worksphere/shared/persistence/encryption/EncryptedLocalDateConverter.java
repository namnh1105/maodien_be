package com.hainam.worksphere.shared.persistence.encryption;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Converter
@Slf4j
public class EncryptedLocalDateConverter implements AttributeConverter<LocalDate, String> {

    private static AesGcmStringEncryptor encryptor;

    @Autowired
    public void setEncryptor(AesGcmStringEncryptor encryptor) {
        EncryptedLocalDateConverter.encryptor = encryptor;
        log.debug("EncryptedLocalDateConverter initialized with encryptor");
    }

    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        if (attribute == null) {
            return null;
        }
        if (encryptor == null) {
            log.warn("EncryptedLocalDateConverter: Encryptor not initialized, returning plain text");
            return attribute.toString();
        }
        try {
            return encryptor.encrypt(attribute.toString());
        } catch (Exception e) {
            log.error("Encryption failed for date: {}", e.getMessage());
            return attribute.toString();
        }
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        if (encryptor == null) {
            log.warn("EncryptedLocalDateConverter: Encryptor not initialized, trying to parse raw data");
            return parseLocalDate(dbData);
        }
        try {
            String plainDate = encryptor.decrypt(dbData);
            return parseLocalDate(plainDate);
        } catch (Exception e) {
            log.error("Decryption failed for date data: {}", e.getMessage());
            return parseLocalDate(dbData); // Try to parse as raw text
        }
    }

    private LocalDate parseLocalDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            log.error("Failed to parse date: {}", dateStr);
            return null;
        }
    }
}
