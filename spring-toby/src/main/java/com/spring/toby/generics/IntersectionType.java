package com.spring.toby.generics;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 여러 타입을 모두 만족하는 하나의 타입
 */
public class IntersectionType {

    // Function을 extends해도 궁극적으로 Function의 apply method 1개를 가지고 있기 때문에 T extends Function & Hello & Hi이렇게 해도 apply method 하나로 간주하여 인터섹션 타입이 된다/
    interface Hello extends Function {
        default void hello() {
            System.out.println("Hello");
        }
    }

    // Function을 extends해도 궁극적으로 Function의 apply method 1개를 가지고 있기 때문에 T extends Function & Hello & Hi이렇게 해도 apply method 하나로 간주하여 인터섹션 타입이 된다/
    interface Hi extends Function{
        default void hi() {
            System.out.println("Hi");
        }
    }

    interface Printer {
        default void print(String str) {
            System.out.println(str);
        }
    }


    // 인터섹션 타입을 이용해서 디폴트 메서드를 가진 인터페이스를 추가했더니 인터페이스 기능 + 디폴트 메서드의 기능을 다합쳐 사용할 수 있는 새로운 객체를 만들 수 있게 되었음
    // Hello와 Hi가 Function을 extends하고 있기 때문에 Function의 apply 메서드를 가지고 있는데 각각 apply method를 가지고 있다고 해서 총 3개의 메서드를 가지고 있는게 아니라 모두 Function의 apply 메서드를 가지고 있기 때문에 궁극적으로 메서드 1개를 가지고 있다고 취급한다.
    private static <T extends Function & Hello & Hi> void hello(T t) {

        // 추가적으로 default 메서드를 사용할 수 있다.
        t.hello();
        t.hi();
    }

    // (Function & Hello & Hi) s -> s 이런식으로 작성하는 람다식은 Function을 구현하고 있음
    // Consumer는 Functional interface이며 내부를 보면 파마리터를 받아서 아무것도 반환하지 않는 accept 메서드를 가지고 있음(반대는 Supplier. Supplier는 파라미터를 받지 않고 반환만 하는 get 메서드를 가지고 있음)
    // callback 방식
    private static <T extends Function> void run(T t, Consumer<T> consumer) {
        consumer.accept(t);
    }



    public static void main(String[] args) {
        // hello((Function & Serializable) s -> s); // Function 안에 메서드 1개 그리고 Serializable 안에 메서드는 한개도 없기 때문에 두개 합치면 메서드가 총 1개이므로 람다식 만족

        // 람다식에 적용되는 Functional interface는 interface가 구현해야될 일반 메소드 정의가 1개여야 한다는 뜻. default 메소드는 제외
        // 단순히 파라미터를 넣으면 파라미터를 반환하는 로직
        hello((Function & Hello & Hi) s -> s);

        // o의 타입은 타입 캐스팅을 안해줘도 타입 추론을 통해 이미 Function & Hello & Hi 타입인걸 알고 있다. 따라서 Hello, Hi의 default 메서드를 사용할 수 있다.
        run((Function & Hello & Hi & Printer) s -> s, o -> {
            o.hello();
            o.hi();
            o.print("Lambda");
        });
    }
}
