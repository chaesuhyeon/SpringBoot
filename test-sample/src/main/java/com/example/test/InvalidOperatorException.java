package com.example.test;

public class InvalidOperatorException extends RuntimeException{
    public InvalidOperatorException() {
        super("Invalid operator, you need to choose one of (+, -, *, /)");
    }
}
