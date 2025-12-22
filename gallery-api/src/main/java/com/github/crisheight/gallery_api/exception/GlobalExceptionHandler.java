package com.github.crisheight.gallery_api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle File Too Large (Return 413)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "File is too large!");
        response.put("message", "Please upload a file smaller than 10MB."); // Match your app.properties limit
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }

    // Handle Duplicate Database Entries
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEntry(DataIntegrityViolationException exc) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Duplicate Entry");
        response.put("message", "An image with this filename already exists.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Handle General Server Errors (Return 500)
    // Catches the DB errors (DataIntegrityViolation) and creates a clean JSON response
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception exc) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", exc.getMessage()); // Or a generic message if you want to hide details
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}