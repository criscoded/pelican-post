package com.github.crisheight.gallery_api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Map<String, String>> error(HttpStatus status, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("timestamp", Instant.now().toString());
        response.put("status", String.valueOf(status.value()));
        response.put("error", message);
        return ResponseEntity.status(status).body(response);
    }

    // Handle File Too Large (Return 413)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return error(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large! Please upload a file smaller than 5MB.");
    }

    // Handle Image Not Found / Not Owned (Return 404 - no existence disclosure)
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleImageNotFound(ImageNotFoundException exc) {
        return error(HttpStatus.NOT_FOUND, "Image not found");
    }

    // Handle Unsupported Upload Content Type (Return 415)
    @ExceptionHandler(UnsupportedContentTypeException.class)
    public ResponseEntity<Map<String, String>> handleUnsupportedContentType(UnsupportedContentTypeException exc) {
        return error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only JPEG, PNG, WebP, GIF and AVIF images are allowed.");
    }

    // Handle Validation Failures (Return 400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException exc) {
        String message = exc.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getDefaultMessage())
                .orElse("Invalid request");
        return error(HttpStatus.BAD_REQUEST, message);
    }

    // Handle Duplicate Database Entries (Return 409)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEntry(DataIntegrityViolationException exc) {
        return error(HttpStatus.CONFLICT, "This resource already exists.");
    }

    // Handle Bad Requests (Return 400)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException exc) {
        return error(HttpStatus.BAD_REQUEST, exc.getMessage());
    }

    // Handle Unauthorized (Return 401) - never disclose why
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthentication(AuthenticationException exc) {
        return error(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    // Handle Forbidden (Return 403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(AccessDeniedException exc) {
        return error(HttpStatus.FORBIDDEN, "Forbidden");
    }

    // Handle General Server Errors (Return 500) - never leak internals
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception exc) {
        log.error("Unhandled exception", exc);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }
} // End GlobalExceptionHandler