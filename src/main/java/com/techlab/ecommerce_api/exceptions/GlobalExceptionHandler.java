package com.techlab.ecommerce_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.techlab.ecommerce_api.models.dto.ErrorResponseDTO;
import com.techlab.ecommerce_api.models.dto.ErrorResponseRecordDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorResponseDTO> handleStockInsuficiente(StockInsuficienteException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Stock insuficiente", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseRecordDTO> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponseRecordDTO error = new ErrorResponseRecordDTO("Argumento inválido", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseRecordDTO> handleGenericException(Exception ex) {
        ErrorResponseRecordDTO error = new ErrorResponseRecordDTO(
                "Error interno del servidor",
                "Ocurrió un error inesperado");
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}