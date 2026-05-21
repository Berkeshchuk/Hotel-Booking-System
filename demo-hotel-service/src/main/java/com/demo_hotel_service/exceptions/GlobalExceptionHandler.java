package com.demo_hotel_service.exceptions;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request - Валідація DTO (@Valid / @Validated)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Збираємо всі помилки в один рядок, розділений \n
        String combinedErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("\n"));
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(combinedErrors));
    }

    // 400 Bad Request - Валідація колекцій або параметрів шляху
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse<String>> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        String combinedErrors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    // Дістаємо тільки фінальну назву поля замість повного шляху
                    String path = violation.getPropertyPath().toString();
                    String field = path.substring(path.lastIndexOf('.') + 1);
                    return field + ": " + violation.getMessage();
                })
                .collect(Collectors.joining("\n"));
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(combinedErrors));
    }

    // 400 Bad Request - Неправильний формат JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse<String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse<>("Невірний формат запиту. Перевірте типи даних у формі."));
    }

    // 400 Bad Request - Відсутній обов'язковий Query параметр
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse<String>> handleMissingParams(MissingServletRequestParameterException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse<>("Відсутній обов'язковий параметр запиту: " + ex.getParameterName()));
    }

    // 400 Bad Request - Неправильний тип параметру
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse<String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ex.printStackTrace();
        // Сховали getRequiredType().getSimpleName(), щоб не світити Java-типи (Long, Integer)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse<>("Некоректне значення для параметру: " + ex.getName()));
    }

    // 400 Bad Request - Бізнес-валідація (IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(ex.getMessage()));
    }

    // 400 Bad Request - Бізнес-логіка (IllegalStateException)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse<String>> handleIllegalStateException(IllegalStateException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(ex.getMessage()));
    }

    // 403 Forbidden - Помилка доступу
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse<String>> handleAccessDenied(AccessDeniedException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse<>("У вас немає прав для виконання цієї дії."));
    }

    // 404 Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse<String>> handleEntityNotFound(EntityNotFoundException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse<>(ex.getMessage()));
    }

    // 409 Conflict
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse<String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse<>("Неможливо виконати операцію: ресурс має пов'язані дані в системі."));
    }
    // 409 Conflict
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse<String>> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse<>("Неможливо виконати операцію: " + ex.getMessage()));
    }



    // 500 Internal Server Error - Для всього іншого
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<String>> handleGeneralException(Exception ex) {
        ex.printStackTrace(); // Логуємо в консоль для розробника
        // ВАЖЛИВО: прибрали ex.getMessage(), щоб клієнт не бачив системні помилки
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse<>("Виникла непередбачена внутрішня помилка сервера. Спробуйте пізніше."));
    }
}

record ErrorResponse<T>(T error) {}