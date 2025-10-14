package com.audiometria.audiometria.api.pagination;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public enum FieldType {


    BOOLEAN {
        public Object parse(String value) {
            return Boolean.valueOf(value);
        }
    },

    CHAR {
        public Object parse(String value) {
            return value.charAt(0);
        }
    },

    DATE {
        public Object parse(String value) {
            try {
                return LocalDateTime.parse(value); // Usa ISO 8601 por defecto
            } catch (Exception e) {
                log.warn("Failed to parse DATE value '{}': {}", value, e.getMessage());
                return null;
            }
        }
    },


    DOUBLE {
        public Object parse(String value) {
            return Double.valueOf(value);
        }
    },

    INTEGER {
        public Object parse(String value) {
            return Integer.valueOf(value);
        }
    },

    LONG {
        public Object parse(String value) {
            return Long.valueOf(value);
        }
    },

    STRING {
        public Object parse(String value) {
            return value;
        }
    },
    UUID {
        public Object parse(String value) {
            log.info("Parseando UUID: {}", value);
            return java.util.UUID.fromString(value); // ✅ Esto es correcto
        }
    },;

    public abstract Object parse(String value);
}
