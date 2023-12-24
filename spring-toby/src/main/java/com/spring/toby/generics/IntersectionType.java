package com.spring.toby.generics;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 여러 타입을 모두 만족하는 하나의 타입
 */
public class IntersectionType {

    interface Hello {
        default void hello() {
            System.out.println("Hello");
        }
    }

    interface Hi {
        default void hi() {
            System.out.println("Hi");
        }
    }


    // 인터섹션 타입을 이용해서 디폴트 메서드를 가진 인터페이스를 추가했더니 인터페이스 기능 + 디폴트 메서드의 기능을 다합쳐 사용할 수 있는 새로운 객체를 만들 수 있게 되었음
    private static <T extends Function & Hello & Hi> void hello(T t) {

        // 추가적으로 default 메서드를 사용할 수 있다.
        t.hello();
        t.hi();
    }


    public static void main(String[] args) {
        // hello((Function & Serializable) s -> s); // Function 안에 메서드 1개 그리고 Serializable 안에 메서드는 한개도 없기 때문에 두개 합치면 메서드가 총 1개이므로 람다식 만족

        // 람다식에 적용되는 Functional interface는 interface가 구현해야될 일반 메소드 정의가 1개여야 한다는 뜻. default 메소드는 제외
        // 단순히 파라미터를 넣으면 파라미터를 반환하는 로직
        hello((Function & Hello & Hi) s -> s);
    }
}
