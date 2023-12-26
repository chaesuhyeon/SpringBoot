package com.spring.toby.generics;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 여러 타입을 모두 만족하는 하나의 타입
 */
public class IntersectionType2 {


    interface DelegateTo<T> {
        T delegate();
    }

    interface Hello extends DelegateTo<String> {
         default void hello() {
             System.out.println("Hello " + delegate()); // Deletegate를 상속받았기 때문에 delegate() 메서드를 사용할 수 있음
         }
    }

    interface UpperCase extends DelegateTo<String> {
        default void upperCase() {
            System.out.println(delegate().toUpperCase());
        }
    }

    // T 자체가 DelegateTo<> 이기 때문에 또 T를 넣을 수 없어서 S를 사용
    private static <T extends DelegateTo<S>, S> void run(T t, Consumer<T> consumer) {
        consumer.accept(t);
    }

    public static void main(String[] args) {
        run((DelegateTo<String> & Hello & UpperCase) ()-> "Daniel", o-> {
            o.hello(); // Hello Daniel
            o.upperCase(); // DANIEL
        });
    }
}
