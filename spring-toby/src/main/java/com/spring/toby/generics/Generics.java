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


    // 이 로직만 봤을 때 <T> (제네릭 메소드) 를 사용하는 것은 목적상 맞지 않는다.
    // 내부 로직에서 실제로 T라는 타입을 사용하고 있지 않기 때문인데 이런 경우에는 isEmpty2 메소드처럼 wildcard를 사용하는 것이 좋다.
    static <T> boolean isEmpty1(List<T> list) {
        return list.size() == 0;
    }

    static <T> long frequency1(List<T> list, T elem) {
        return list.stream().filter(s -> s.equals(elem)).count();
    }

    static <T extends Comparable<T>> T max1 (List<T> list) {
        return list.stream().reduce((a, b) -> a.compareTo(b) > 0 ? a : b).get();
    }


    // wildcard
    // ?에 어떤 타입이 와도 상관 없다.
    // List<?>는 List<Integer>의 상위 타입이다.
    static void printList2(List<?> list) {
        list.forEach(System.out::println);
    }

    static boolean isEmpty2(List<?> list) { // List의 size 메소드는 타입과 상관없는 단순한 List의 메소드이기 때문에 <?> 이렇게 와일드 카드를 사용해도 무방
        return list.size() == 0;
    }

    static long frequency2(List<?> list, Object elem) {
        return list.stream().filter(s -> s.equals(elem)).count();
    }

    static <T extends Comparable<? super T>> T max2 (List<? extends T> list) {
        return list.stream().reduce((a, b) -> a.compareTo(b) > 0 ? a : b).get();
    }

    public static void main(String[] args) {
        Integer[] arr1 = new Integer[]{1, 2, 3, 4, 5};
        System.out.println(countGreaterThan(arr1, 4));

        String[] arr2 = new String[]{"a", "b", "c", "d", "e"};
        System.out.println(countGreaterThan(arr2, "c"));

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
//        printList1(list); // 컴파일 에러. List<Object>는 List<Integer>의 상위 타입이 아니다.
        printList2(list);

        System.out.println(isEmpty2(list));
        System.out.println(frequency1(list, 3));
        System.out.println(max1(list));
    }
}
