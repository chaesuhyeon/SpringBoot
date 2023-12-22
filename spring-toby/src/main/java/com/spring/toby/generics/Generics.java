package com.spring.toby.generics;

import java.util.Arrays;
import java.util.List;

public class Generics {

    // method generics
    static <T> void print(T t) {
        System.out.println(t.toString());
    }

    // constructor generics
    public <S> Generics(S s) {

    }

    // bounded type parameter
    static <T extends List> void  print(T t) {}

    // bounded type parameter 응용
    // Comparable 인터페이스를 구현한 클래스만 사용 가능
    static <T extends Comparable<T>> long countGreaterThan(T[] arr, T elem) {
        return Arrays.stream(arr).filter(s -> s.compareTo(elem) > 0).count();
    }

    // wildcard와 차이점을 보여주기 위해 선언
    // List<Object>는 List<Integer>의 상위 타입이 아니다.
    static void printList1(List<Object> list) {
        list.forEach(System.out::println);
    }


    // wildcard
    // ?에 어떤 타입이 와도 상관 없다.
    // List<?>는 List<Integer>의 상위 타입이다.
    static void printList2(List<?> list) {
        list.forEach(System.out::println);
    }


    public static void main(String[] args) {
        Integer[] arr1 = new Integer[]{1, 2, 3, 4, 5};
        System.out.println(countGreaterThan(arr1, 4));

        String[] arr2 = new String[]{"a", "b", "c", "d", "e"};
        System.out.println(countGreaterThan(arr2, "c"));

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
//        printList1(list); // 컴파일 에러. List<Object>는 List<Integer>의 상위 타입이 아니다.
        printList2(list);
    }
}
