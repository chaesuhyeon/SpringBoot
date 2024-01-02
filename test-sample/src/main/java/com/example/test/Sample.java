package com.example.test;

import java.util.Scanner;

public class Sample {
    public static void main(String[] args) {
        CalculationRequest calculationRequest = new CalculationRequestReader().read();

        long answer = new Calculator().calculate(calculationRequest.getNum1(), calculationRequest.getOperator(), calculationRequest.getNum2());

        System.out.println(answer);
    }
}
