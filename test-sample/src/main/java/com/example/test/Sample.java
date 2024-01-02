package com.example.test;

import java.util.Scanner;

public class Sample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter two numbers and an oprator (e.g. 1 2 +): ");

        String result = scanner.nextLine();
        String[] parts = result.split(" ");
        long num1 = Long.parseLong(parts[0]);
        long num2 = Long.parseLong(parts[2]);
        String operator = parts[1];

        long answer = new Calculator().calculate(num1, operator, num2);

        System.out.println(answer);
    }
}
