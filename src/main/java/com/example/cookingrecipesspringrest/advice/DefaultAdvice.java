package com.example.cookingrecipesspringrest.advice;

import com.example.cookingrecipesspringrest.dto.ExceptionResponseDTO;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionResponseDTO> handleServiceException(ServiceException e) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<ExceptionResponseDTO> handleRepositoryException(RepositoryException e) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(
                "неверный тип данных " + e.getPropertyName() + ": " + e.getValue());
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(
                e.getMessage() + ". Supported: " + e.getSupportedHttpMethods());
        return new ResponseEntity<>(responseDTO, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleNoHandlerFoundException(NoHandlerFoundException e) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.METHOD_NOT_ALLOWED);
    }

}
