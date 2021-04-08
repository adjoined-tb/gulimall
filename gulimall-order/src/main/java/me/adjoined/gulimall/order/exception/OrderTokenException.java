package me.adjoined.gulimall.order.exception;

public class OrderTokenException extends RuntimeException {
    public OrderTokenException() {
        super("token mismatch");
    }
}
