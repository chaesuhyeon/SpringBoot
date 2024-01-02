package com.example.test;

public class CalculationRequest {
    private final long num1;
    private final String operator;
    private final long num2;

    public CalculationRequest(String[] parts) {
        if(parts.length != 3) {
            throw new BadRequestException();
        }

        String operator = parts[1];

        // 연산자의 길이가 1이 아니거나, 연산자가 +, -, *, / 가 아닌 경우
        if(operator.length() != 1 || isInvalidOperator(operator)) {
            throw new InvalidOperatorException();
        }

        this.num1 = Long.parseLong(parts[0]);
        this.num2 = Long.parseLong(parts[2]);
        this.operator = operator;
    }

    private static boolean isInvalidOperator(String operator) {
        return !operator.equals("+") &&
                !operator.equals("-") &&
                !operator.equals("*") &&
                !operator.equals("/");
    }

    public long getNum1() {
        return num1;
    }

    public String getOperator() {
        return operator;
    }

    public long getNum2() {
        return num2;
    }
}
