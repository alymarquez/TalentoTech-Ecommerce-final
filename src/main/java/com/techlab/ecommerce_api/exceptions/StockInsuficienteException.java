package com.techlab.ecommerce_api.exceptions;

public class StockInsuficienteException extends RuntimeException {
    
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
    
    public StockInsuficienteException(String producto, Integer stockDisponible, Integer stockSolicitado) {
        super(String.format("Stock insuficiente para producto %s. Stock disponible: %d, solicitado: %d", 
                producto, stockDisponible, stockSolicitado));
    }
}