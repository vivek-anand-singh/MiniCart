package com.example.minicart.exception;

public class CartQuantityException extends RuntimeException {
    public CartQuantityException(String message) {
        super(message);
    }
}
